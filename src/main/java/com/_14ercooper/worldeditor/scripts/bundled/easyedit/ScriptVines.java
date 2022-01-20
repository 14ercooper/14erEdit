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

public class ScriptVines extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, CommandSender player, String label) {
        try {
            String radius = args.get(0);
            String length = args.get(1);
            String variance = args.get(2);
            String density, block = "";
            if (args.size() > 3) {
                density = args.get(3);
                if (args.size() > 4) {
                    block = args.get(4);
                } else {
                    block = "vine";
                }
            } else {
                density = "0.2";
            }
            Bukkit.getServer().dispatchCommand(player, "fx br s 0 0.5 $ vines{" + radius + ";" + length + ";" + variance + ";"
                    + density + ";" + block + "}");
        } catch (Exception e) {
            Main.logError("Could not parse vine script. Did you pass the correct arguments?", player, e);
        }
    }

}
