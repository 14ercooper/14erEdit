package com._14ercooper.worldeditor.main;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.macros.MacroLauncher;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.scripts.CraftscriptManager;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.Random;
import java.util.Set;

public class GlobalVars {

    // Global variables
    public static int noiseSeed;
    public static SimplexNoise simplexNoise;
    public static Plugin plugin;
    public static final Random rand = new Random();
    public static int minorVer = 0;
    public static int majorVer = 0;

    // What blocks should be targeted?
    public static Set<Material> brushMask;

    // Managers
    public static CraftscriptManager scriptManager;
    public static MacroLauncher macroLauncher;
    public static Parser operationParser;
    public static IteratorManager iteratorManager;

    // For debugging
    public static boolean isDebug = false;

    // Make macros not crash the server
    public static boolean countEdits = false;

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
    public static boolean outputStacktrace = false;

    // Caps for edits
    public static long minEditX = Long.MIN_VALUE;
    public static long minEditY = -1;
    public static long minEditZ = Long.MIN_VALUE;
    public static long maxEditX = Long.MAX_VALUE;
    public static long maxEditY = 256;
    public static long maxEditZ = Long.MAX_VALUE;
}
