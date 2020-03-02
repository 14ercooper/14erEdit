package fourteener.worldeditor.main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import fourteener.worldeditor.commands.CommandFx;
import fourteener.worldeditor.commands.CommandRun;
import fourteener.worldeditor.commands.CommandScript;
import fourteener.worldeditor.commands.CommandUndo;
import fourteener.worldeditor.worldeditor.brush.BrushListener;
import fourteener.worldeditor.worldeditor.scripts.CraftscriptManager;
import fourteener.worldeditor.worldeditor.scripts.bundled.easyedit.*;
import fourteener.worldeditor.worldeditor.scripts.bundled.quickbrush.*;
import fourteener.worldeditor.worldeditor.scripts.bundled.selection.*;
import fourteener.worldeditor.worldeditor.selection.SelectionWandListener;

public class Main extends JavaPlugin {
	// Global variables
	public static World world;
	public static SimplexNoise simplexNoise;
	public static CraftscriptManager scriptManager;
	
	// For debugging
	public static boolean isDebug = true;
	
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
		world = Bukkit.getWorlds().get(0);
		simplexNoise = new SimplexNoise (world.getSeed()); // Seeded using the world seed for variance between worlds but consistency in the same world
		
		// Load the craftscripts manager
		scriptManager = new CraftscriptManager();
		
		// Register the prepackaged craftscripts
		quickbrush();
		selection();
		easyedit();
	}

	private void easyedit() {
		// Easyedit script bundle
		scriptManager.registerCraftscript("erode", new ScriptErode());
		scriptManager.registerCraftscript("tree", new ScriptTree());
		scriptManager.registerCraftscript("grassbrush", new ScriptGrassBrush());
		scriptManager.registerCraftscript("vines", new ScriptVines());
		scriptManager.registerCraftscript("biome", new ScriptBiome());
		ScriptFlatten scriptFlatten = new ScriptFlatten();
		scriptManager.registerCraftscript("flatten", scriptFlatten);
		scriptManager.registerCraftscript("absflatten", scriptFlatten);
		ScriptOverlay scriptOverlay = new ScriptOverlay();
		scriptManager.registerCraftscript("overlay", scriptOverlay);
		ScriptLine scriptLine = new ScriptLine();
		scriptManager.registerCraftscript("line", scriptLine);
	}

	private void selection() {
		// Selection script bundle
		scriptManager.registerCraftscript("set", new ScriptSet());
		scriptManager.registerCraftscript("replace", new ScriptReplace());
		scriptManager.registerCraftscript("grass", new ScriptGrass());
	}

	private void quickbrush() {
		// Quick brush script bundle
		ScriptBallBrushSet bbset = new ScriptBallBrushSet();
		scriptManager.registerCraftscript("ballset", bbset);
		scriptManager.registerCraftscript("bset", bbset);
		ScriptSquareBrushSet bsset = new ScriptSquareBrushSet();
		scriptManager.registerCraftscript("squareset", bsset);
		scriptManager.registerCraftscript("sset", bsset);
		ScriptDiamondBrushSet bdset = new ScriptDiamondBrushSet();
		scriptManager.registerCraftscript("diamondset", bdset);
		scriptManager.registerCraftscript("dset", bdset);
		ScriptHollowBrushSet bhset = new ScriptHollowBrushSet();
		scriptManager.registerCraftscript("hollowset", bhset);
		scriptManager.registerCraftscript("hset", bhset);
		ScriptEllipseBrushSet beset = new ScriptEllipseBrushSet();
		scriptManager.registerCraftscript("ellipseset", beset);
		scriptManager.registerCraftscript("eset", beset);
		ScriptBallBrushReplace bbrep = new ScriptBallBrushReplace();
		scriptManager.registerCraftscript("ballreplace", bbrep);
		scriptManager.registerCraftscript("brep", bbrep);
		ScriptSquareBrushReplace bsrep = new ScriptSquareBrushReplace();
		scriptManager.registerCraftscript("squarereplace", bsrep);
		scriptManager.registerCraftscript("srep", bsrep);
		ScriptDiamondBrushReplace bdrep = new ScriptDiamondBrushReplace();
		scriptManager.registerCraftscript("diamondreplace", bdrep);
		scriptManager.registerCraftscript("drep", bdrep);
		ScriptHollowBrushReplace bhrep = new ScriptHollowBrushReplace();
		scriptManager.registerCraftscript("hollowreplace", bhrep);
		scriptManager.registerCraftscript("hrep", bhrep);
		ScriptEllipseBrushReplace berep = new ScriptEllipseBrushReplace();
		scriptManager.registerCraftscript("ellipsereplace", berep);
		scriptManager.registerCraftscript("erep", berep);
	}
	
	@Override
	public void onDisable () {
		
	}
	
	public static void logDebug (String message) {
		if (isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] " + message); // ----
	}
}
