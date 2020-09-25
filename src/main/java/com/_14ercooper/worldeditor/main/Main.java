package com._14ercooper.worldeditor.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.brush.BrushListener;
import com._14ercooper.worldeditor.commands.*;
import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.macros.MacroLauncher;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.scripts.CraftscriptManager;
import com._14ercooper.worldeditor.selection.SelectionWandListener;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
	// Create folders as needed
	try {
	    Files.createDirectories(Paths.get("plugins/14erEdit/schematics"));
	    Files.createDirectories(Paths.get("plugins/14erEdit/ops"));
	    Files.createDirectories(Paths.get("plugins/14erEdit/Commands"));
	    Files.createDirectories(Paths.get("plugins/14erEdit/vars"));
	    Files.createDirectories(Paths.get("plugins/14erEdit/templates"));
	    Files.createDirectories(Paths.get("plugins/14erEdit/multibrushes"));
	    Files.createDirectories(Paths.get("plugins/14erEdit/functions"));
	}
	catch (IOException e) {
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
	this.getCommand("14erEdit").setExecutor(new CommandInfo());
	this.getCommand("async").setExecutor(new CommandAsync());
	this.getCommand("brmask").setExecutor(new CommandBrmask());
	this.getCommand("template").setExecutor(new CommandTemplate());
	this.getCommand("funct").setExecutor(new CommandFunction());

	// Set up brush mask
	GlobalVars.brushMask = new HashSet<Material>();
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
	GlobalVars.plugin = this;

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
    }

    @Override
    public void onDisable() {
	// We don't need to do anything on disable
    }

    public static void logDebug(String message) {
	if (GlobalVars.isDebug)
	    Bukkit.getServer().broadcastMessage("§c[DEBUG] " + message); // ----
    }

    public static void logError(String message, CommandSender p) {
	if (p == null)
	    p = Bukkit.getConsoleSender();
	p.sendMessage("§6[ERROR] " + message);
    }

    private static void loadConfig() {
	GlobalVars.plugin.saveDefaultConfig();

	GlobalVars.undoLimit = GlobalVars.plugin.getConfig().getLong("undoLimit");
	GlobalVars.blocksPerAsync = GlobalVars.plugin.getConfig().getLong("blocksPerAsync");
	GlobalVars.ticksPerAsync = GlobalVars.plugin.getConfig().getLong("ticksPerAsync");
    }
}
