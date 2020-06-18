package com.fourteener.worldeditor.main;

import com.fourteener.worldeditor.macros.macros.nature.*;
import com.fourteener.worldeditor.macros.macros.technical.*;

public class MacroLoader {

	public static void LoadMacros () {
		GlobalVars.macroLauncher.addMacro("erode", new ErodeMacro());
		GlobalVars.macroLauncher.addMacro("adverode", new AdvancedErodeMacro());
		GlobalVars.macroLauncher.addMacro("tree", new BasicTreeMacro());
		GlobalVars.macroLauncher.addMacro("biome", new BiomeMacro());
		GlobalVars.macroLauncher.addMacro("grass", new GrassMacro());
		GlobalVars.macroLauncher.addMacro("vines", new VinesMacro());
		GlobalVars.macroLauncher.addMacro("flatten", new FlattenMacro());
		GlobalVars.macroLauncher.addMacro("schem", new SchematicMacro());
		GlobalVars.macroLauncher.addMacro("line_old", new LineMacro());
		GlobalVars.macroLauncher.addMacro("line", new LineBrushMacro());
		GlobalVars.macroLauncher.addMacro("catenary", new CatenaryMacro());
	}
}
