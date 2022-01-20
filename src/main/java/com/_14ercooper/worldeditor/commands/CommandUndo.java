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

package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.undo.UndoSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

// These are dedicated versions of the undo and redo commands
public class CommandUndo implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.isOp()) {
                sender.sendMessage("You must be opped to use 14erEdit");
                return false;
            }
        }

        if (command.getName().equalsIgnoreCase("un")) {
            int numToUndo = 1;
            try {
                numToUndo = Integer.parseInt(args[0]);
            } catch (Exception e) {
                numToUndo = 1;
            }
            return UndoSystem.findUserUndo(sender).undoChanges(numToUndo);
        } else if (command.getName().equalsIgnoreCase("re")) {
            int numToRedo = 1;
            try {
                numToRedo = Integer.parseInt(args[0]);
            } catch (Exception e) {
                numToRedo = 1;
            }
            return UndoSystem.findUserUndo(sender).redoChanges(numToRedo);
        }
        return false;
    }

    public static class TabComplete implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            List<String> tabArgs = new ArrayList<>();

            if (args.length == 1) {
                tabArgs.add("[number]");
            }

            return tabArgs;
        }
    }
}
