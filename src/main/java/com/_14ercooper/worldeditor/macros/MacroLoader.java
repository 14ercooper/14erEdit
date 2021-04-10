package com._14ercooper.worldeditor.macros;

import com._14ercooper.worldeditor.macros.macros.nature.AdvancedErodeMacro;
import com._14ercooper.worldeditor.macros.macros.nature.BasicTreeMacro;
import com._14ercooper.worldeditor.macros.macros.nature.BiomeMacro;
import com._14ercooper.worldeditor.macros.macros.nature.ErodeMacro;
import com._14ercooper.worldeditor.macros.macros.nature.GrassMacro;
import com._14ercooper.worldeditor.macros.macros.nature.VinesMacro;
import com._14ercooper.worldeditor.macros.macros.technical.CatenaryMacro;
import com._14ercooper.worldeditor.macros.macros.technical.FlattenMacro;
import com._14ercooper.worldeditor.macros.macros.technical.LineBrushMacro;
import com._14ercooper.worldeditor.macros.macros.technical.LineMacro;
import com._14ercooper.worldeditor.macros.macros.technical.SchematicMacro;
import com._14ercooper.worldeditor.main.GlobalVars;

public class MacroLoader {

    public static void LoadMacros() {
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
