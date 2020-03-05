package fourteener.worldeditor.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fourteener.worldeditor.commands.CommandFx;
import fourteener.worldeditor.commands.CommandRun;
import fourteener.worldeditor.commands.CommandScript;
import fourteener.worldeditor.commands.CommandUndo;
import fourteener.worldeditor.operations.Parser;
import fourteener.worldeditor.worldeditor.brush.BrushListener;
import fourteener.worldeditor.worldeditor.macros.MacroLauncher;
import fourteener.worldeditor.worldeditor.scripts.CraftscriptManager;
import fourteener.worldeditor.worldeditor.selection.SelectionWandListener;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable () {
		// Register commands with the server
		this.getCommand("fx").setExecutor(new CommandFx());
		CommandUndo undoCmd = new CommandUndo();
		this.getCommand("un").setExecutor(undoCmd);
		this.getCommand("re").setExecutor(undoCmd);
		this.getCommand("script").setExecutor(new CommandScript());
		this.getCommand("run").setExecutor(new CommandRun());
		
		// Register listeners for brushes and wands
		getServer().getPluginManager().registerEvents(new SelectionWandListener(), this);
		getServer().getPluginManager().registerEvents(new BrushListener(), this);
		
		// These are needed by the plugin, but should only be loaded once as they are very slow to load
		GlobalVars.world = Bukkit.getWorlds().get(0);
		GlobalVars.simplexNoise = new SimplexNoise (GlobalVars.world.getSeed()); // Seeded using the world seed for variance between worlds but consistency in the same world
		
		// Load managers
		GlobalVars.scriptManager = new CraftscriptManager();
		GlobalVars.macroLauncher = new MacroLauncher();
		GlobalVars.operationParser = new Parser();
		
		// Register the prepackaged things to managers
		CraftscriptLoader.LoadBundledCraftscripts();
		MacroLoader.LoadMacros();
		BrushLoader.LoadBrushes();
		OperatorLoader.LoadOperators();
	}
	
	@Override
	public void onDisable () {
		
	}
	
	public static void logDebug (String message) {
		if (GlobalVars.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] " + message); // ----
	}
}
