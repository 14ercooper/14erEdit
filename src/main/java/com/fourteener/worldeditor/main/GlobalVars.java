package com.fourteener.worldeditor.main;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.fourteener.worldeditor.async.AsyncManager;
import com.fourteener.worldeditor.blockiterator.IteratorManager;
import com.fourteener.worldeditor.macros.MacroLauncher;
import com.fourteener.worldeditor.operations.Parser;
import com.fourteener.worldeditor.scripts.CraftscriptManager;
import com.fourteener.worldeditor.undo.Undo;

public class GlobalVars {
	
	// Global variables
	public static World world;
	public static SimplexNoise simplexNoise;
	public static Plugin plugin;
	
	// Used to track undos
	public static Undo currentUndo = null;
	
	// Target liquids?
	public static boolean targetLiquid = false;
	
	// Managers
	public static CraftscriptManager scriptManager;
	public static MacroLauncher macroLauncher;
	public static Parser operationParser;
	public static AsyncManager asyncManager;
	public static IteratorManager iteratorManager;
	
	// For debugging
	public static boolean isDebug = false;
	
	// Configs
	public static long undoLimit = 250000;
	public static long blocksPerAsync = 10000;
	public static long ticksPerAsync = 4;
}
