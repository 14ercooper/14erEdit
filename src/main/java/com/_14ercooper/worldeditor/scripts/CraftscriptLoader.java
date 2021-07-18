package com._14ercooper.worldeditor.scripts;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptBiome;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptCaternary;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptErode;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptLine;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptOverlay;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptTree;
import com._14ercooper.worldeditor.scripts.bundled.easyedit.ScriptVines;

public class CraftscriptLoader {

    public static void LoadBundledCraftscripts() {
        EasyEdit();
    }

    private static void EasyEdit() {
        // Easyedit script bundle
        GlobalVars.scriptManager.registerCraftscript("erode", new ScriptErode());
        GlobalVars.scriptManager.registerCraftscript("tree", new ScriptTree());
        GlobalVars.scriptManager.registerCraftscript("vines", new ScriptVines());
        GlobalVars.scriptManager.registerCraftscript("biome", new ScriptBiome());
        GlobalVars.scriptManager.registerCraftscript("overlay", new ScriptOverlay());
        GlobalVars.scriptManager.registerCraftscript("line", new ScriptLine());
        ScriptCaternary scriptCaternary = new ScriptCaternary();
        GlobalVars.scriptManager.registerCraftscript("catenary", scriptCaternary);
        GlobalVars.scriptManager.registerCraftscript("cat", scriptCaternary);
    }
}
