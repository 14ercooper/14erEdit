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

package com._14ercooper.worldeditor.selection;

import com._14ercooper.math.Point3;
import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class MultiselectCommandHandler {

    public static boolean handleCommand(SelectionManager manager, String player, String[] args) {
        if (args[1].equalsIgnoreCase("spline") || args[1].equalsIgnoreCase("uniformspline")
                || args[1].equalsIgnoreCase("chordalspline")) {

            Operator op = parseOp(args, 2, player);

            List<String> iteratorArgs = new ArrayList<>();
            if (args[1].equalsIgnoreCase("spline")) {
                iteratorArgs.add("0.5");
            }
            else if (args[1].equalsIgnoreCase("uniformspline")) {
                iteratorArgs.add("0");
            }
            else if (args[1].equalsIgnoreCase("chordalspline")) {
                iteratorArgs.add("1");
            }

            // Convert all points to args
            for (Point3 point : manager.getAllPoints()) {
                iteratorArgs.add(point.toString());
            }

            BlockIterator iter = Objects.requireNonNull(IteratorManager.INSTANCE.getIterator("spline")).newIterator(iteratorArgs,
                    Objects.requireNonNull(Bukkit.getServer().getPlayer(UUID.fromString(player))).getWorld(), Bukkit.getConsoleSender());

            AsyncManager.scheduleEdit(op, Bukkit.getServer().getPlayer(UUID.fromString(player)), iter);
            return true;
        }
        return false;
    }

    public static Operator parseOp(String[] args, int startIndex, String player) {
        Player p = Bukkit.getServer().getPlayer(UUID.fromString(player));

        List<String> opArray = new LinkedList<>(Arrays.asList(args));
        while (startIndex > 0) {
            opArray.remove(0);
            startIndex--;
        }

        StringBuilder opStr = new StringBuilder();
        for (String s : opArray) {
            opStr.append(s);
            opStr.append(" ");
        }

        return new Operator(opStr.toString(), p);
    }
}
