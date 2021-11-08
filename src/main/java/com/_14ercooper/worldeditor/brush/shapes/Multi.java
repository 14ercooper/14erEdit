package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Multi extends BrushShape {

    String file = "";
    final List<String> args = new ArrayList<>();
    List<BlockIterator> iters = null;
    List<Operator> ops = null;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z, World world, CommandSender sender) {
        Main.logError("Multibrush used in a normal brush context. This is an error.", Bukkit.getConsoleSender(), null);
        return null;
    }

    @Override
    public void addNewArgument(String argument) {
        if (file.isEmpty()) {
            file = argument;
        } else {
            args.add(argument);
        }
    }

    @Override
    public boolean lastInputProcessed() {
        return true;
    }

    @Override
    public boolean gotEnoughArgs() {
        return file.isEmpty();
    }

    public List<BlockIterator> getIters(double x, double y, double z, World world, CommandSender sender) {
        genMultibrush(x, y, z, world, sender);
        return iters;
    }

    public List<Operator> getOps(double x, double y, double z) {
        return ops;
    }

    @SuppressWarnings("unused")
    private void genMultibrush(double x, double y, double z, World world, CommandSender sender) {
        // Create the lists
        iters = new ArrayList<>();
        ops = new ArrayList<>();

        // Get the file name
        String filename = "plugins/14erEdit/multibrushes/" + file;
        if (Files.exists(Paths.get(filename))) {
            // We're already good
        } else if (Files.exists(Paths.get(filename + ".mb"))) {
            filename += ".mb";
        } else if (Files.exists(Paths.get(filename + ".txt"))) {
            filename += ".txt";
        }

        // Load the set of brushes from the file
        List<String> brushesRaw = new ArrayList<>();
        try {
            brushesRaw = Files.readAllLines(Paths.get(filename));
        } catch (IOException e1) {
            Main.logError("Error loading multibrush file: " + filename, Brush.currentPlayer, e1);
        }

        // Perform templating
        List<String> brushes = new LinkedList<>();
        for (String s : brushesRaw) {
            if (!s.isEmpty()) {
                for (int i = args.size(); i > 0; i--) {
                    s = s.replaceAll("\\$" + (i), args.get(i - 1));
                }
                brushes.add(s);
            }
        }

        // Parse each brush shape and operator
        for (String s : brushes) {
            String[] rawData = s.split(" ");
            int brushOpOffset = 1; // Used to remove brush parameters from the operation

            // Get the shape generator, and store the args
            BrushShape shapeGenerator = Brush.GetBrushShape(rawData[0]);
            try {
                do {
                    if (rawData[brushOpOffset].equalsIgnoreCase("end")) {
                        brushOpOffset += 2;
                        break;
                    }
                    shapeGenerator.addNewArgument(rawData[brushOpOffset]);
                    brushOpOffset++;
                }
                while (shapeGenerator.lastInputProcessed());
                brushOpOffset--;
            } catch (Exception e) {
                Main.logError(
                        "Could not parse brush arguments. Please check that you provided enough numerical arguments for the brush shape.",
                        Brush.currentPlayer, e);
                return;
            }

            if (shapeGenerator.gotEnoughArgs()) {
                Main.logError("Not enough inputs to the brush shape were provided. Please provide enough inputs.",
                        Brush.currentPlayer, null);
            }

            // Construct the operator
            // Start by removing brush parameters
            List<String> opArray = new LinkedList<>(Arrays.asList(rawData));
            while (brushOpOffset > 0) {
                opArray.remove(0);
                brushOpOffset--;
            }
            // Construct the string
            String opStr = "";
            for (String str : opArray) {
                opStr = opStr.concat(str).concat(" ");
            }
            // And then construct the operator
            Operator operation = new Operator(opStr, Brush.currentPlayer);

            // Add to the lists
            iters.add(shapeGenerator.GetBlocks(x, y, z, world, sender));
            ops.add(operation);
        }
    }

    @Override
    public int minArgCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int operatorCount() {
        return 0;
    }

    @Override
    public void runBrush(List<Operator> operators, double x, double y, double z, Player currentPlayer) {
        // Create a multi-operator async chain
        Main.logDebug("Multibrush file: " + file);
        Main.logDebug("Number of arguments to multibrush: " + args.size());
        List<BlockIterator> iters = getIters(x, y, z, currentPlayer.getWorld(), currentPlayer);
        Main.logDebug("Number of iters in multibrush: " + iters.size());
        List<Operator> ops = getOps(x, y, z);
        Main.logDebug("Number of ops in multibrush: " + ops.size());

        AsyncManager.scheduleEdit(iters, ops, currentPlayer);
    }
}

//else {
//	// Mark as multibrush
//	multibrush = true;
//
//	// Get the file name
//	String filename = "plugins/14erEdit/multibrushes/" + brushOperation[2];
//	if (Files.exists(Paths.get(filename))) {
//	    // We're already good
//	}
//	else if (Files.exists(Paths.get(filename + ".mb"))) {
//	    filename += ".mb";
//	}
//	else if (Files.exists(Paths.get(filename + ".txt"))) {
//	    filename += ".txt";
//	}
//
//	// Load the set of brushes from the file
//	List<String> brushesRaw = Files.readAllLines(Paths.get(filename));
//
//	// Perform templating
//	List<String> brushes = new LinkedList<String>();
//	for (String s : brushesRaw) {
//	    if (!s.isEmpty()) {
//		for (int i = brushOperation.length - 1; i >= 2; i--) {
//		    s = s.replaceAll("\\$" + (i - 2), brushOperation[i]);
//		}
//		brushes.add(s);
//	    }
//	}
//
//	// Parse each brush shape and operator
//	for (String s : brushes) {
//	    String[] rawData = s.split(" ");
//	    brushOpOffset = 1; // Used to remove brush parameters from the operation
//
//	    // Get the shape generator, and store the args
//	    shapeGenerator = brushShapes.get(rawData[0]);
//	    LinkedList<Double> thisShapeArgs = new LinkedList<Double>();
//	    try {
//		for (int i = 0; i < shapeGenerator.GetArgCount(); i++) {
//		    thisShapeArgs.add(Double.parseDouble(rawData[1 + i]));
//		    brushOpOffset++;
//		}
//	    }
//	    catch (Exception e) {
//		Main.logError(
//			"Could not parse brush arguments. Please check that you provided enough numerical arguments for the brush shape.",
//			player);
//		return;
//	    }
//
//	    // Construct the operator
//	    // Start by removing brush parameters
//	    List<String> opArray = new LinkedList<String>(Arrays.asList(rawData));
//	    while (brushOpOffset > 0) {
//		opArray.remove(0);
//		brushOpOffset--;
//	    }
//	    // Construct the string
//	    String opStr = "";
//	    for (String str : opArray) {
//		opStr = opStr.concat(str).concat(" ");
//	    }
//	    // And then construct the operator
//	    operation = new Operator(opStr, player);
//
//	    // Invalid operator?
//	    if (operation == null)
//		continue;
//
//	    // Add to the lists
//	    shapeGenerators.add(shapeGenerator);
//	    shapeArgList.add(thisShapeArgs);
//	    operations.add(operation);
//	}
//
//	// Save the brush
//	BrushListener.brushes.add(this);
//	player.sendMessage(
//		"§aNOTE: Multibrushes run in large edit mode, so no undo will be registered. Please be careful.");
//	player.sendMessage("§dBrush created and bound to item in hand.");
//	GlobalVars.errorLogged = false;
//}
