package com._14ercooper.worldeditor.scripts;

import com._14ercooper.worldeditor.scripts.bundled.easyedit.*;

public class CraftscriptLoader {

    public static void LoadBundledCraftscripts() {
        EasyEdit();
    }

    private static void EasyEdit() {
        // Easyedit script bundle
        CraftscriptManager.INSTANCE.registerCraftscript("erode", new ScriptErode());
        CraftscriptManager.INSTANCE.registerCraftscript("tree", new ScriptTree());
        CraftscriptManager.INSTANCE.registerCraftscript("vines", new ScriptVines());
        CraftscriptManager.INSTANCE.registerCraftscript("biome", new ScriptBiome());
        CraftscriptManager.INSTANCE.registerCraftscript("overlay", new ScriptOverlay());
        CraftscriptManager.INSTANCE.registerCraftscript("line", new ScriptLine());
        ScriptCaternary scriptCaternary = new ScriptCaternary();
        CraftscriptManager.INSTANCE.registerCraftscript("catenary", scriptCaternary);
        CraftscriptManager.INSTANCE.registerCraftscript("cat", scriptCaternary);
    }
}
