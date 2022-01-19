// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.macros;

import com._14ercooper.worldeditor.macros.macros.nature.BasicTreeMacro;
import com._14ercooper.worldeditor.macros.macros.nature.BiomeMacro;
import com._14ercooper.worldeditor.macros.macros.nature.ErodeMacro;
import com._14ercooper.worldeditor.macros.macros.nature.VinesMacro;
import com._14ercooper.worldeditor.macros.macros.technical.CatenaryMacro;
import com._14ercooper.worldeditor.macros.macros.technical.LineBrushMacro;
import com._14ercooper.worldeditor.macros.macros.technical.LineMacro;
import com._14ercooper.worldeditor.macros.macros.technical.SchematicMacro;

public class MacroLoader {

    public static void LoadMacros() {
        MacroLauncher.INSTANCE.addMacro("erode", new ErodeMacro());
        MacroLauncher.INSTANCE.addMacro("tree", new BasicTreeMacro());
        MacroLauncher.INSTANCE.addMacro("biome", new BiomeMacro());
        MacroLauncher.INSTANCE.addMacro("vines", new VinesMacro());
        MacroLauncher.INSTANCE.addMacro("schem", new SchematicMacro());
        MacroLauncher.INSTANCE.addMacro("line_old", new LineMacro());
        MacroLauncher.INSTANCE.addMacro("line", new LineBrushMacro());
        MacroLauncher.INSTANCE.addMacro("catenary", new CatenaryMacro());
    }
}
