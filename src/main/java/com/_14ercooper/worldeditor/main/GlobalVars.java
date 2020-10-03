package com._14ercooper.worldeditor.main;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.macros.MacroLauncher;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.scripts.CraftscriptManager;
import com._14ercooper.worldeditor.undo.Undo;

public class GlobalVars {

    // Global variables
    public static int noiseSeed;
    public static SimplexNoise simplexNoise;
    public static Plugin plugin;

    // Used to track undos
    public static Undo currentUndo = null;

    // What blocks should be targeted?
    public static Set<Material> brushMask;

    // Managers
    public static CraftscriptManager scriptManager;
    public static MacroLauncher macroLauncher;
    public static Parser operationParser;
    public static AsyncManager asyncManager;
    public static IteratorManager iteratorManager;

    // For debugging
    public static boolean isDebug = false;

    // This may be useful
    public static boolean autoConfirm = false;

    // Configs
    public static long undoLimit = 250000;
    public static long blocksPerAsync = 10000;
    public static long ticksPerAsync = 4;
    public static long maxLoopLength = 5000;
    public static long maxFunctionIters = 100000;
    public static boolean logDebugs = false;
    public static boolean logErrors = true;

    // No error spam
    public static boolean errorLogged = false;
}
