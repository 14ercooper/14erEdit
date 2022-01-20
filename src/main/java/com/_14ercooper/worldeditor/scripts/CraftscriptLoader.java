/**
 * This file is part of 14erEdit.
 *
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

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
