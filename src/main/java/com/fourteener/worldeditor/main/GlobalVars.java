package com.fourteener.worldeditor.main;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.fourteener.worldeditor.async.AsyncManager;
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
	
	// Managers
	public static CraftscriptManager scriptManager;
	public static MacroLauncher macroLauncher;
	public static Parser operationParser;
	public static AsyncManager asyncManager;
	
	// For debugging
	public static boolean isDebug = false;
}
