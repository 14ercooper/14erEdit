package com.fourteener.worldeditor.main;

import com.fourteener.worldeditor.worldeditor.scripts.bundled.easyedit.*;
import com.fourteener.worldeditor.worldeditor.scripts.bundled.quickbrush.*;
import com.fourteener.worldeditor.worldeditor.scripts.bundled.selection.*;

public class CraftscriptLoader {
	
	public static void LoadBundledCraftscripts() {
		EasyEdit();
		Selection();
		QuickBrush();
	}

	private static void EasyEdit() {
		// Easyedit script bundle
		GlobalVars.scriptManager.registerCraftscript("erode", new ScriptErode());
		GlobalVars.scriptManager.registerCraftscript("tree", new ScriptTree());
		GlobalVars.scriptManager.registerCraftscript("grassbrush", new ScriptGrassBrush());
		GlobalVars.scriptManager.registerCraftscript("vines", new ScriptVines());
		GlobalVars.scriptManager.registerCraftscript("biome", new ScriptBiome());
		ScriptFlatten scriptFlatten = new ScriptFlatten();
		GlobalVars.scriptManager.registerCraftscript("flatten", scriptFlatten);
		GlobalVars.scriptManager.registerCraftscript("absflatten", scriptFlatten);
		ScriptOverlay scriptOverlay = new ScriptOverlay();
		GlobalVars.scriptManager.registerCraftscript("overlay", scriptOverlay);
		ScriptLine scriptLine = new ScriptLine();
		GlobalVars.scriptManager.registerCraftscript("line", scriptLine);
	}

	private static void Selection() {
		// Selection script bundle
		GlobalVars.scriptManager.registerCraftscript("set", new ScriptSet());
		GlobalVars.scriptManager.registerCraftscript("replace", new ScriptReplace());
		GlobalVars.scriptManager.registerCraftscript("grass", new ScriptGrass());
	}

	private static void QuickBrush() {
		// Quick brush script bundle
		ScriptBallBrushSet bbset = new ScriptBallBrushSet();
		GlobalVars.scriptManager.registerCraftscript("ballset", bbset);
		GlobalVars.scriptManager.registerCraftscript("bset", bbset);
		ScriptSquareBrushSet bsset = new ScriptSquareBrushSet();
		GlobalVars.scriptManager.registerCraftscript("squareset", bsset);
		GlobalVars.scriptManager.registerCraftscript("sset", bsset);
		ScriptDiamondBrushSet bdset = new ScriptDiamondBrushSet();
		GlobalVars.scriptManager.registerCraftscript("diamondset", bdset);
		GlobalVars.scriptManager.registerCraftscript("dset", bdset);
		ScriptHollowBrushSet bhset = new ScriptHollowBrushSet();
		GlobalVars.scriptManager.registerCraftscript("hollowset", bhset);
		GlobalVars.scriptManager.registerCraftscript("hset", bhset);
		ScriptEllipseBrushSet beset = new ScriptEllipseBrushSet();
		GlobalVars.scriptManager.registerCraftscript("ellipseset", beset);
		GlobalVars.scriptManager.registerCraftscript("eset", beset);
		ScriptBallBrushReplace bbrep = new ScriptBallBrushReplace();
		GlobalVars.scriptManager.registerCraftscript("ballreplace", bbrep);
		GlobalVars.scriptManager.registerCraftscript("brep", bbrep);
		ScriptSquareBrushReplace bsrep = new ScriptSquareBrushReplace();
		GlobalVars.scriptManager.registerCraftscript("squarereplace", bsrep);
		GlobalVars.scriptManager.registerCraftscript("srep", bsrep);
		ScriptDiamondBrushReplace bdrep = new ScriptDiamondBrushReplace();
		GlobalVars.scriptManager.registerCraftscript("diamondreplace", bdrep);
		GlobalVars.scriptManager.registerCraftscript("drep", bdrep);
		ScriptHollowBrushReplace bhrep = new ScriptHollowBrushReplace();
		GlobalVars.scriptManager.registerCraftscript("hollowreplace", bhrep);
		GlobalVars.scriptManager.registerCraftscript("hrep", bhrep);
		ScriptEllipseBrushReplace berep = new ScriptEllipseBrushReplace();
		GlobalVars.scriptManager.registerCraftscript("ellipsereplace", berep);
		GlobalVars.scriptManager.registerCraftscript("erep", berep);
	}
}
