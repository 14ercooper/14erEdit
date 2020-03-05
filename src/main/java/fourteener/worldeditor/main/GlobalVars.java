package fourteener.worldeditor.main;

import org.bukkit.World;

import fourteener.worldeditor.operations.Parser;
import fourteener.worldeditor.worldeditor.macros.MacroLauncher;
import fourteener.worldeditor.worldeditor.scripts.CraftscriptManager;

public class GlobalVars {

	// Global variables
	public static World world;
	public static SimplexNoise simplexNoise;
	
	// Managers
	public static CraftscriptManager scriptManager;
	public static MacroLauncher macroLauncher;
	public static Parser operationParser;
	
	// For debugging
	public static boolean isDebug = true;
}
