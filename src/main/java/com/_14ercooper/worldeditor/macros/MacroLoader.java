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
