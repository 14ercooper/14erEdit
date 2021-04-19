package com._14ercooper.worldeditor.scripts;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptBiome;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptCaternary;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptErode;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptFlatten;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptGrassBrush;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptLine;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptOverlay;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptTree;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptVines;
import com._14ercooper.worldeditor.scripts.bundled.selection.ScriptClone;
import com._14ercooper.worldeditor.scripts.bundled.selection.ScriptGrass;
import com._14ercooper.worldeditor.scripts.bundled.selection.ScriptMegaoperate;

public class CraftscriptLoader {

    public static void LoadBundledCraftscripts() {
        EasyEdit();
        Selection();
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
        GlobalVars.scriptManager.registerCraftscript("overlay", new ScriptOverlay());
        GlobalVars.scriptManager.registerCraftscript("line", new ScriptLine());
        ScriptCaternary scriptCaternary = new ScriptCaternary();
        GlobalVars.scriptManager.registerCraftscript("catenary", scriptCaternary);
        GlobalVars.scriptManager.registerCraftscript("cat", scriptCaternary);
    }

    private static void Selection() {
        // Selection script bundle
        GlobalVars.scriptManager.registerCraftscript("grass", new ScriptGrass());
        GlobalVars.scriptManager.registerCraftscript("clone", new ScriptClone());
        GlobalVars.scriptManager.registerCraftscript("operate", new ScriptMegaoperate());
    }
}
