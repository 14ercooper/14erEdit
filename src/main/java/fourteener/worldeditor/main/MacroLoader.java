package fourteener.worldeditor.main;

import fourteener.worldeditor.worldeditor.macros.macros.nature.*;
import fourteener.worldeditor.worldeditor.macros.macros.technical.*;

public class MacroLoader {

	public static void LoadMacros () {
		Main.macroLauncher.addMacro("erode", new ErodeMacro());
		Main.macroLauncher.addMacro("tree", new BasicTreeMacro());
		Main.macroLauncher.addMacro("biome", new BiomeMacro());
		Main.macroLauncher.addMacro("grass", new GrassMacro());
		Main.macroLauncher.addMacro("vines", new VinesMacro());
		Main.macroLauncher.addMacro("flatten", new FlattenMacro());
		Main.macroLauncher.addMacro("schem", new SchematicMacro());
	}
}
