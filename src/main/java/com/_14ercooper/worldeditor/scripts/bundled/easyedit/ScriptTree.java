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

package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.scripts.Craftscript;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;

public class ScriptTree extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, CommandSender player, String label) {
        try {
            String treeType = args.get(0);
            String treeSize = args.get(1);
            String treeLeaves;
            String treeWood;
            double treeSizeVariance = Double.parseDouble(treeSize) * 0.25;
            if (args.size() == 2) {
                treeLeaves = "lime_wool";
                treeWood = "brown_wool";
            } else {
                try {
                    treeLeaves = args.get(2);
                    treeWood = args.get(3);
                } catch (Exception e) {
                    Main.logError(
                            "Error parsing tree script. If you provide leaves, you must provide wood block material as well.",
                            player, e);
                    return;
                }
            }
            Bukkit.getServer().dispatchCommand(player, "fx br s 0 0.5 $ tree{" + treeType + ";" + treeLeaves + ";" + treeWood + ";"
                    + treeSize + ";" + treeSizeVariance + "}");
        } catch (Exception e) {
            Main.logError("Error parsing tree script. Did you provide the correct arguments?", player, e);
        }
    }
}
