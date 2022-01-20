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

public class ScriptOverlay extends Craftscript {

    // Args block depth air
    @Override
    public void perform(LinkedList<String> args, CommandSender player, String label) {
        try {
            String radius = args.get(0);
            String block = args.get(1);
            int depth = Integer.parseInt(args.get(2));
            int air;
            if (args.size() > 3) {
                air = Integer.parseInt(args.get(3));
            } else {
                air = 3;
            }

            StringBuilder command = new StringBuilder("fx br s " + radius + " 0.5 ? ~ air ");
            for (int i = 0; i < depth; i++) {
                command.append("| ? ^ - ").append(i + 1).append(" ").append(air + i - 1).append(" air > ").append(block).append(" : false ");
            }
            command.append(": false : false");
            Main.logDebug("Overlay command: " + command);

            Bukkit.getServer().dispatchCommand(player, command.toString());
        } catch (Exception e) {
            Main.logError("Could not parse overlay macro. Did you provide the correct arguments?",
                    player, e);
        }
    }
}
