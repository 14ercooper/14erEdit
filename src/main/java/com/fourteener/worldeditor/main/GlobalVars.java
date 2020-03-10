package com.fourteener.worldeditor.main;

import org.bukkit.World;

import com.fourteener.worldeditor.operations.Parser;
import com.fourteener.worldeditor.worldeditor.macros.MacroLauncher;
import com.fourteener.worldeditor.worldeditor.scripts.CraftscriptManager;

public class GlobalVars {

	// Global variables
	public static World world;
	public static SimplexNoise simplexNoise;
	
	// Managers
	public static CraftscriptManager scriptManager;
	public static MacroLauncher macroLauncher;
	public static Parser operationParser;
	
	// For debugging
	public static boolean isDebug = false;
}
