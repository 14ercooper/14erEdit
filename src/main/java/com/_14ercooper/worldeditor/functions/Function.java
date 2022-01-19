// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.functions;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.brush.BrushListener;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Function {

    public static long maxFunctionIters = 100000;

    // Interpreter commands map
    public static final Map<String, InterpreterCommand> commands = new HashMap<>();
    public static final List<Function> waitingFunctions = new LinkedList<>();
    // Function-specific variables
    public final List<Double> variables = new ArrayList<>();
    public static final List<Double> globalVariables = new ArrayList<>();
    public final List<Double> variableStack = new ArrayList<>();
    public final List<String> dataSegment = new LinkedList<>();
    public final Map<String, Integer> labelsMap = new HashMap<>();
    public final List<String> templateArgs = new LinkedList<>();
    public final CommandSender player;
    public final Block target;
    public final boolean isOperator;
    public double ra, cmpres;
    public int waitDelay = 0;
    public boolean exit = false;
    public int currentLine = -1; // Incremented as the first step
    public double exitVal = 1;
    long iters = 0;
    // Operator state
    public OperatorState operatorState;

    public Function(String filename, List<String> args, CommandSender player, boolean isOperator, OperatorState opState) {
        // Set constant variables
        templateArgs.addAll(args);
        this.player = player;
        this.isOperator = isOperator;
        this.operatorState = opState;

        if (player instanceof Player) {
            target = BrushListener.getTargetBlock((Player) player);
        } else {
            target = Bukkit.getServer().getWorlds().get(0).getBlockAt(0, 0, 0);
        }

        for (int i = 0; i < 1000; i++) {
            variables.add(0.0);
        }

        if (globalVariables.size() == 0) {
            for (int i = 0; i < 100; i++) {
                globalVariables.add(0.0);
            }
        }

        // Load data from file
        filename = "plugins/14erEdit/functions/" + filename;
        if (Files.exists(Paths.get(filename))) {
            //
        } else if (Files.exists(Paths.get(filename + ".fx"))) {
            filename += ".fx";
        } else if (Files.exists(Paths.get(filename + ".14fdl"))) {
            filename += ".14fdl";
        } else if (Files.exists(Paths.get(filename + ".txt"))) {
            filename += ".txt";
        } else if (Files.exists(Paths.get(filename + ".mcfunction"))) {
            filename += ".mcfunction";
        } else {
            Main.logError("Function file not found.", player, null);
            return;
        }
        try {
            dataSegment.addAll(Files.readAllLines(Paths.get(filename)));
        } catch (IOException e) {
            Main.logError("Could not open function file.", player, e);
            return;
        }

        // Generate labels map
        for (int i = 0; i < dataSegment.size(); i++) {
            String s = dataSegment.get(i).replaceAll("^\\s+", "");
            if (!s.isBlank() && s.charAt(0) == ':') {
                labelsMap.put(s.substring(1), i);
            }
        }

        Main.logDebug("Function loaded from " + filename);
        Main.logDebug("Number of labels: " + labelsMap.size());
    }

    // Setup all functions
    public static void SetupFunctions() {
        RegisterFunctions.RegisterAll();

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), Function::CheckWaitingFunctions, 1L, 1L);
    }

    // See if any waiting functions are ready to keep running
    public static void CheckWaitingFunctions() {
        for (int i = 0; i < waitingFunctions.size(); i++) {
            boolean didStart = waitingFunctions.get(i).checkCallback(i);
            if (didStart) {
                Main.logDebug("Resuming paused function.");
                i--;
            }
        }
    }

    public double run() {
        try {
            // Until pause or exit, loop
            while (!exit && waitDelay == 0) {
                // Increment current line
                currentLine++;
                iters++;

                // Exit if ran off function
                if (currentLine >= dataSegment.size()) {
                    Main.logDebug("Ran off end of function. Exiting.");
                    exit = true;
                    continue;
                }

                // Log error and exit if running too long
                if (iters > maxFunctionIters) {
                    exit = true;
                    Main.logError("Function max iterations exceeded.", player, null);
                    continue;
                }

                // Process template variables
                String line = dataSegment.get(currentLine).replaceAll("^\\s+", "");
                for (int i = templateArgs.size(); i >= 1; i--) {
                    line = line.replaceAll("\\$" + i, templateArgs.get(i - 1));
                }

                // Split into list
                List<String> lineContents = new ArrayList<>(Arrays.asList(line.split("\\s")));

                // If blank line
                if (lineContents.get(0).isEmpty()) {
                    Main.logDebug("Empty line processed.");
                    continue;
                }

                // If comment or label
                if (lineContents.get(0).charAt(0) == '#' || lineContents.get(0).charAt(0) == ':') {
                    Main.logDebug("Comment or label processed.");
                    continue;
                }

                // If interpreter command
                if (commands.containsKey(lineContents.get(0).replaceFirst("!", ""))) {
                    String functName = lineContents.get(0).replaceFirst("!", "");
                    lineContents.remove(0);
                    Main.logDebug("Running interpreter command: " + functName);
                    commands.get(functName).run(lineContents, this);
                    continue;
                }

                // Else (Minecraft command)
                Main.logDebug("Running as normal command.");
                boolean didRun = Bukkit.dispatchCommand(player, line);
                if (!didRun) {
                    Main.logError("Invalid command detected. Line " + currentLine, player, null);
                }
            }
        } catch (Exception e) {
            Main.logError(
                    "Error executing function. Error on line " + (currentLine + 1) + ".\nError: " + e.getMessage(),
                    player, e);
            return 0;
        }

        // If pause, register callback
        if (waitDelay != 0 && !exit) {
            waitingFunctions.add(this);
        }

        return exitVal;
    }

    public double parseVariable(String var) {
        if (var.contains("$v")) {
            String s = var.substring(2);
            try {
                return variables.get(Integer.parseInt(s));
            } catch (Exception e) {
                Main.logError("Invalid variable: " + var, player, e);
                return 0;
            }
        }
        if (var.contains("$gv")) {
            String s = var.substring(2);
            try {
                return globalVariables.get(Integer.parseInt(s));
            } catch (Exception e) {
                Main.logError("Invalid variable: " + var, player, e);
                return 0;
            }
        }
        if (var.equalsIgnoreCase("$ra")) {
            return ra;
        }
        if (var.equalsIgnoreCase("$cmpres")) {
            return cmpres;
        }
        try {
            return Double.parseDouble(var);
        } catch (Exception e) {
            Main.logError("Invalid number: " + var, player, e);
            return 0;
        }
    }

    public void setVariable(String var, double num) {
        if (var.contains("$v")) {
            String s = var.substring(2);
            try {
                variables.set(Integer.parseInt(s), num);
            } catch (Exception e) {
                Main.logError("Invalid variable: " + var, player, e);
            }
            return;
        }
        if (var.contains("$gv")) {
            String s = var.substring(2);
            try {
                globalVariables.set(Integer.parseInt(s), num);
            } catch (Exception e) {
                Main.logError("Invalid variable: " + var, player, e);
            }
            return;
        }
        if (var.equalsIgnoreCase("$ra")) {
            ra = num;
            return;
        }
        if (var.equalsIgnoreCase("$cmpres")) {
            cmpres = num;
            return;
        }
        Main.logError("Unknown variable: " + var, player, null);
    }

    public boolean checkCallback(int waitPos) {
        if (waitDelay > 0) {
            waitDelay--;
        }
        if (waitDelay == 0) {
            waitingFunctions.remove(waitPos);
            run();
            return true;
        } else if (waitDelay < 0) {
            if (AsyncManager.getRemainingBlocks() == 0) {
                waitingFunctions.remove(waitPos);
                waitDelay = 0;
                run();
                return true;
            }
        }
        return false;
    }
}
