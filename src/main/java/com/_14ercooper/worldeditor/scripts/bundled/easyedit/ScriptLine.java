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
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class ScriptLine extends Craftscript {

    // Args block depth air
    @Override
    public void perform(LinkedList<String> args, CommandSender player, String label) {
        try {
            String block = args.get(0);
            int length;
            if (args.size() > 1) {
                length = Integer.parseInt(args.get(1));
            } else {
                length = 10;
            }

            Vector blockPos, playerPos;
            if (player instanceof Player) {
                blockPos = ((Player) player).getLocation().getDirection();
                playerPos = ((Player) player).getLocation().toVector();
            } else {
                blockPos = new Vector(0, 0, 0);
                playerPos = new Vector(0, 0, 0);
            }

            int x1 = (int) playerPos.getX();
            int y1 = (int) playerPos.getY() + 1;
            int z1 = (int) playerPos.getZ();
            int x2 = (int) (blockPos.getX() * -1 * length + playerPos.getX());
            int y2 = (int) (blockPos.getY() * -1 * length + playerPos.getY());
            int z2 = (int) (blockPos.getZ() * -1 * length + playerPos.getZ());

            Bukkit.getServer().dispatchCommand(player,
                    "run $ line_old{" + x1 + ";" + y1 + ";" + z1 + ";" + x2 + ";" + y2 + ";" + z2 + ";" + block + "}");

        } catch (Exception e) {
            Main.logError("Could not parse line macro. Did you provide a block material and optionally length?",
                    player, e);
        }
    }
}
