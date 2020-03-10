package com.fourteener.worldeditor.main;

import com.fourteener.worldeditor.worldeditor.macros.macros.nature.*;
import com.fourteener.worldeditor.worldeditor.macros.macros.technical.*;

public class MacroLoader {

	public static void LoadMacros () {
		GlobalVars.macroLauncher.addMacro("erode", new ErodeMacro());
		GlobalVars.macroLauncher.addMacro("tree", new BasicTreeMacro());
		GlobalVars.macroLauncher.addMacro("biome", new BiomeMacro());
		GlobalVars.macroLauncher.addMacro("grass", new GrassMacro());
		GlobalVars.macroLauncher.addMacro("vines", new VinesMacro());
		GlobalVars.macroLauncher.addMacro("flatten", new FlattenMacro());
		GlobalVars.macroLauncher.addMacro("schem", new SchematicMacro());
	}
}
