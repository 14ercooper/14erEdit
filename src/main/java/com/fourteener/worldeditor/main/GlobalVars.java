package com.fourteener.worldeditor.main;

import org.bukkit.World;

import com.fourteener.worldeditor.macros.MacroLauncher;
import com.fourteener.worldeditor.operations.Parser;
import com.fourteener.worldeditor.scripts.CraftscriptManager;
import com.fourteener.worldeditor.undo.Undo;

public class GlobalVars {
	
	// Global variables
	public static World world;
	public static SimplexNoise simplexNoise;
	
	// This is probably a bad idea
	public static Undo currentUndo = null;
	
	// Managers
	public static CraftscriptManager scriptManager;
	public static MacroLauncher macroLauncher;
	public static Parser operationParser;
	
	// For debugging
	public static boolean isDebug = false;
}
