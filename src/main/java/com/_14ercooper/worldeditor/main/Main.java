package com._14ercooper.worldeditor.main;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.IteratorLoader;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.brush.BrushListener;
import com._14ercooper.worldeditor.brush.BrushLoader;
import com._14ercooper.worldeditor.commands.*;
import com._14ercooper.worldeditor.compat.WorldEditCompat;
import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.macros.MacroLauncher;
import com._14ercooper.worldeditor.macros.MacroLoader;
import com._14ercooper.worldeditor.operations.OperatorLoader;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.scripts.CraftscriptLoader;
import com._14ercooper.worldeditor.scripts.CraftscriptManager;
import com._14ercooper.worldeditor.selection.SelectionWandListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;

public class Main extends JavaPlugin {

    public static void logError(String message, CommandSender p, Exception e) {
        GlobalVars.errorLogged = true;
        if (p == null)
            p = Bukkit.getConsoleSender();
        String stackTrace = "";
        if (e != null && GlobalVars.outputStacktrace) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            stackTrace = "\n" + sw;
        }
        p.sendMessage("§6[ERROR] " + message + stackTrace);
        if (GlobalVars.logErrors) {
            try {
                String errMessage = "";
                errMessage += message + stackTrace + "\n";

                if (!Files.exists(Paths.get("plugins/14erEdit/error.log")))
                    Files.createFile(Paths.get("plugins/14erEdit/error.log"));
                Files.write(Paths.get("plugins/14erEdit/error.log"), errMessage.getBytes(), StandardOpenOption.APPEND);

            } catch (Exception e2) {
                // Also not super important
            }
        }
    }

    @Override
    public void onDisable() {
        // We don't need to do anything on disable
    }

    public static int randRange(int min, int max) {
        if (min == max) {
            return min;
        }
        return min + GlobalVars.rand.nextInt(max - min + 1);
    }

    static String debugText = "";

    public static void logDebug(String message) {
        if (GlobalVars.isDebug)
            debugText += "§c[DEBUG] " + message + "\n"; // ----
        try {
            if (GlobalVars.logDebugs) {
                if (!Files.exists(Paths.get("plugins/14erEdit/debug.log")))
                    Files.createFile(Paths.get("plugins/14erEdit/debug.log"));
                Files.writeString(Paths.get("plugins/14erEdit/debug.log"), message + "\n", StandardOpenOption.APPEND);
            }
        } catch (Exception e) {
            // Do nothing, this isn't super important
        }
    }

    public static void outputDebug() {
        if (GlobalVars.isDebug && !debugText.isBlank()) {
            Bukkit.getServer().broadcastMessage(debugText);
            debugText = "";
        }
    }

    private static void loadConfig() {
        GlobalVars.plugin.saveDefaultConfig();

        if (!GlobalVars.plugin.getConfig().isSet("maxLoopLength")) {
            System.out.println("Updating configuration file.");
            try {
                String configUpdate1 = "\n# Max execution lengths\n" + "maxLoopLength: 5000\n"
                        + "maxFunctionIters: 100000\n" + "\n" + "# Should debugs/errors be logged to a file?\n"
                        + "logDebugs: false\n" + "logErrors: true\n" + "\n" + "# Should debug/autoconfirm be on by default?\n"
                        + "defaultAutoConfirm: false\n" + "defaultDebug: false\n";
                Files.write(Paths.get("/plugins/14erEdit/config.yml"), configUpdate1.getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println(
                        "Error updating configuration. Please manually delete the existing configuration (14erEdit/config.yml).\n"
                                + "The server will now shut down.");
                try {
                    Thread.sleep(15000);
                    Bukkit.shutdown();
                } catch (InterruptedException e1) {
                    // Do nothing
                }
            }
        }

        GlobalVars.undoLimit = GlobalVars.plugin.getConfig().getLong("undoLimit");
        GlobalVars.blocksPerAsync = GlobalVars.plugin.getConfig().getLong("blocksPerAsync");
        GlobalVars.ticksPerAsync = GlobalVars.plugin.getConfig().getLong("ticksPerAsync");
        GlobalVars.maxLoopLength = GlobalVars.plugin.getConfig().getLong("maxLoopLength");
        GlobalVars.maxFunctionIters = GlobalVars.plugin.getConfig().getLong("maxFunctionIters");
        GlobalVars.logDebugs = GlobalVars.plugin.getConfig().getBoolean("logDebugs");
        GlobalVars.logErrors = GlobalVars.plugin.getConfig().getBoolean("logErrors");
        GlobalVars.autoConfirm = GlobalVars.plugin.getConfig().getBoolean("defaultAutoConfirm");
        GlobalVars.isDebug = GlobalVars.plugin.getConfig().getBoolean("defaultDebug");
    }

    public static boolean inEditRegion(long x, long y, long z) {
        return (x <= GlobalVars.minEditX || y <= GlobalVars.minEditY || z <= GlobalVars.minEditZ)
                || (x >= GlobalVars.maxEditX || y >= GlobalVars.maxEditY || z >= GlobalVars.maxEditZ);
    }

    @Override
    public void onEnable() {
        // Check Java 11 (ensure that the user didn't somehow bypass the earlier class
        // version check)
        float javaVersion = Float.parseFloat(System.getProperty("java.class.version"));
        if (javaVersion < 55) {
            System.out.println(
                    "Java 11 or never is required to run this server. https://adoptopenjdk.net is a good place to find Java 11.\n"
                            + "The server will now shut down.");
            try {
                Thread.sleep(15000);
                Bukkit.shutdown();
            } catch (InterruptedException e1) {
                // Do nothing
            }
        }

        String ver = getServer().getVersion().split("MC: ")[1];
        GlobalVars.majorVer = Integer.parseInt(ver.split("\\.")[1].replaceAll("[^\\d.]", ""));
        GlobalVars.minorVer = Integer.parseInt(ver.split("\\.")[2].replaceAll("[^\\d.]", ""));
        System.out.println("Using version " + getServer().getVersion() + ": " + GlobalVars.majorVer + "/" + GlobalVars.minorVer);

        // Create folders as needed
        try {
            Files.createDirectories(Paths.get("plugins/14erEdit/schematics"));
            Files.createDirectories(Paths.get("plugins/14erEdit/ops"));
            Files.createDirectories(Paths.get("plugins/14erEdit/Commands"));
            Files.createDirectories(Paths.get("plugins/14erEdit/vars"));
            Files.createDirectories(Paths.get("plugins/14erEdit/templates"));
            Files.createDirectories(Paths.get("plugins/14erEdit/multibrushes"));
            Files.createDirectories(Paths.get("plugins/14erEdit/functions"));
        } catch (IOException e) {
            Main.logDebug("Error creating directory structure. 14erEdit may not work properly until this is resolved.");
        }

        // Register commands with the server
        this.getCommand("fx").setExecutor(new CommandFx());
        CommandUndo undoCmd = new CommandUndo();
        this.getCommand("un").setExecutor(undoCmd);
        this.getCommand("re").setExecutor(undoCmd);
        CommandConfirm confirmCmd = new CommandConfirm();
        this.getCommand("confirm").setExecutor(confirmCmd);
        this.getCommand("cancel").setExecutor(confirmCmd);
        this.getCommand("script").setExecutor(new CommandScript());
        this.getCommand("run").setExecutor(new CommandRun());
        this.getCommand("runat").setExecutor(new CommandRunat());
        this.getCommand("debug").setExecutor(new CommandDebug());
        this.getCommand("error").setExecutor(new CommandError());
        this.getCommand("14erEdit").setExecutor(new CommandInfo());
        this.getCommand("async").setExecutor(new CommandAsync());
        this.getCommand("brmask").setExecutor(new CommandBrmask());
        this.getCommand("template").setExecutor(new CommandTemplate());
        this.getCommand("funct").setExecutor(new CommandFunction());
        this.getCommand("limit").setExecutor(new CommandLimit());

        // Set up brush mask
        GlobalVars.brushMask = new HashSet<>();
        GlobalVars.brushMask.add(Material.AIR);
        GlobalVars.brushMask.add(Material.CAVE_AIR);
        GlobalVars.brushMask.add(Material.VOID_AIR);

        // Register listeners for brushes and wands
        getServer().getPluginManager().registerEvents(new SelectionWandListener(), this);
        getServer().getPluginManager().registerEvents(new BrushListener(), this);

        // These are needed by the plugin, but should only be loaded once as they are
        // very slow to load
        GlobalVars.noiseSeed = (int) Bukkit.getWorlds().get(0).getSeed(); // Seeded using the world seed
        // for variance between worlds
        // but consistency in the same
        // world
        GlobalVars.simplexNoise = new SimplexNoise(Bukkit.getWorlds().get(0).getSeed());
        GlobalVars.plugin = this;
        GlobalVars.rand.nextDouble(); // Toss out a value from the LCG

        // Load config
        loadConfig();

        // Load managers
        GlobalVars.scriptManager = new CraftscriptManager();
        GlobalVars.macroLauncher = new MacroLauncher();
        GlobalVars.operationParser = new Parser();
        GlobalVars.asyncManager = new AsyncManager();
        GlobalVars.iteratorManager = new IteratorManager();

        // Register the prepackaged things to managers
        CraftscriptLoader.LoadBundledCraftscripts();
        MacroLoader.LoadMacros();
        BrushLoader.LoadBrushes();
        OperatorLoader.LoadOperators();
        IteratorLoader.LoadIterators();
        Function.SetupFunctions();

        // Initialize the WE syntax compat layer
        WorldEditCompat.init();
    }
}
