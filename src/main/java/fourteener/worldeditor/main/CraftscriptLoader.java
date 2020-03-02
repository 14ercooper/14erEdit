package fourteener.worldeditor.main;

import fourteener.worldeditor.worldeditor.scripts.bundled.easyedit.*;
import fourteener.worldeditor.worldeditor.scripts.bundled.quickbrush.*;
import fourteener.worldeditor.worldeditor.scripts.bundled.selection.*;

public class CraftscriptLoader {
	
	public static void LoadBundledCraftscripts() {
		EasyEdit();
		Selection();
		QuickBrush();
	}

	private static void EasyEdit() {
		// Easyedit script bundle
		Main.scriptManager.registerCraftscript("erode", new ScriptErode());
		Main.scriptManager.registerCraftscript("tree", new ScriptTree());
		Main.scriptManager.registerCraftscript("grassbrush", new ScriptGrassBrush());
		Main.scriptManager.registerCraftscript("vines", new ScriptVines());
		Main.scriptManager.registerCraftscript("biome", new ScriptBiome());
		ScriptFlatten scriptFlatten = new ScriptFlatten();
		Main.scriptManager.registerCraftscript("flatten", scriptFlatten);
		Main.scriptManager.registerCraftscript("absflatten", scriptFlatten);
		ScriptOverlay scriptOverlay = new ScriptOverlay();
		Main.scriptManager.registerCraftscript("overlay", scriptOverlay);
		ScriptLine scriptLine = new ScriptLine();
		Main.scriptManager.registerCraftscript("line", scriptLine);
	}

	private static void Selection() {
		// Selection script bundle
		Main.scriptManager.registerCraftscript("set", new ScriptSet());
		Main.scriptManager.registerCraftscript("replace", new ScriptReplace());
		Main.scriptManager.registerCraftscript("grass", new ScriptGrass());
	}

	private static void QuickBrush() {
		// Quick brush script bundle
		ScriptBallBrushSet bbset = new ScriptBallBrushSet();
		Main.scriptManager.registerCraftscript("ballset", bbset);
		Main.scriptManager.registerCraftscript("bset", bbset);
		ScriptSquareBrushSet bsset = new ScriptSquareBrushSet();
		Main.scriptManager.registerCraftscript("squareset", bsset);
		Main.scriptManager.registerCraftscript("sset", bsset);
		ScriptDiamondBrushSet bdset = new ScriptDiamondBrushSet();
		Main.scriptManager.registerCraftscript("diamondset", bdset);
		Main.scriptManager.registerCraftscript("dset", bdset);
		ScriptHollowBrushSet bhset = new ScriptHollowBrushSet();
		Main.scriptManager.registerCraftscript("hollowset", bhset);
		Main.scriptManager.registerCraftscript("hset", bhset);
		ScriptEllipseBrushSet beset = new ScriptEllipseBrushSet();
		Main.scriptManager.registerCraftscript("ellipseset", beset);
		Main.scriptManager.registerCraftscript("eset", beset);
		ScriptBallBrushReplace bbrep = new ScriptBallBrushReplace();
		Main.scriptManager.registerCraftscript("ballreplace", bbrep);
		Main.scriptManager.registerCraftscript("brep", bbrep);
		ScriptSquareBrushReplace bsrep = new ScriptSquareBrushReplace();
		Main.scriptManager.registerCraftscript("squarereplace", bsrep);
		Main.scriptManager.registerCraftscript("srep", bsrep);
		ScriptDiamondBrushReplace bdrep = new ScriptDiamondBrushReplace();
		Main.scriptManager.registerCraftscript("diamondreplace", bdrep);
		Main.scriptManager.registerCraftscript("drep", bdrep);
		ScriptHollowBrushReplace bhrep = new ScriptHollowBrushReplace();
		Main.scriptManager.registerCraftscript("hollowreplace", bhrep);
		Main.scriptManager.registerCraftscript("hrep", bhrep);
		ScriptEllipseBrushReplace berep = new ScriptEllipseBrushReplace();
		Main.scriptManager.registerCraftscript("ellipsereplace", berep);
		Main.scriptManager.registerCraftscript("erep", berep);
	}
}
