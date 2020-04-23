package com.fourteener.worldeditor.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.fourteener.worldeditor.async.AsyncManager;
import com.fourteener.worldeditor.blockiterator.IteratorManager;
import com.fourteener.worldeditor.brush.BrushListener;
import com.fourteener.worldeditor.commands.*;
import com.fourteener.worldeditor.macros.MacroLauncher;
import com.fourteener.worldeditor.operations.Parser;
import com.fourteener.worldeditor.scripts.CraftscriptManager;
import com.fourteener.worldeditor.selection.SelectionWandListener;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable () {
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
		
		// Register listeners for brushes and wands
		getServer().getPluginManager().registerEvents(new SelectionWandListener(), this);
		getServer().getPluginManager().registerEvents(new BrushListener(), this);
		
		// These are needed by the plugin, but should only be loaded once as they are very slow to load
		GlobalVars.world = Bukkit.getWorlds().get(0);
		GlobalVars.simplexNoise = new SimplexNoise (GlobalVars.world.getSeed()); // Seeded using the world seed for variance between worlds but consistency in the same world
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
	}
	
	@Override
	public void onDisable () {
		
	}
	
	public static void logDebug (String message) {
		if (GlobalVars.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] " + message); // ----
	}
	
	private static void loadConfig () {
		GlobalVars.plugin.saveDefaultConfig();
		
		GlobalVars.undoLimit = GlobalVars.plugin.getConfig().getLong("undoLimit");
		GlobalVars.blocksPerAsync = GlobalVars.plugin.getConfig().getLong("blocksPerAsync");
		GlobalVars.ticksPerAsync = GlobalVars.plugin.getConfig().getLong("ticksPerAsync");
	}
}
