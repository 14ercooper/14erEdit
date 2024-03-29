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

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.player.PlayerManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandGmask implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lavel, String[] args) {
        if (sender instanceof Player) {
            if (!sender.isOp()) {
                sender.sendMessage("You must be opped to use 14erEdit");
                return false;
            }
        }

        try {
            if (args[0].equalsIgnoreCase("clear")) {
                PlayerManager.getPlayerWrapper(sender).resetGlobalMask();
                return true;
            }
            else {
                List<Material> materials = new ArrayList<>();
                for (int i = 0; i < args.length; i++) {
                    String material = args[i];
                    try {
                        Material mat = Material.matchMaterial(material);
                        materials.add(mat);
                    } catch (Exception e) {
                        Main.logError("Unknown material: " + i, sender, e);
                    }
                }
                PlayerManager.getPlayerWrapper(sender).setGlobalMask(materials);
                sender.sendMessage("§dGlobal mask updated");
                return true;
            }
        }
        catch (Exception e) {
            Main.logError("Could not update global mask", sender, e);
            return false;
        }
    }

    public static class TabComplete implements TabCompleter {

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            List<String> tabArgs = new ArrayList<>();

            if (args.length == 1) {
                tabArgs.addAll(Main.getBlockNames(args[args.length - 1]));
                tabArgs.add("clear");
            }
            else if (args.length > 1) {
                tabArgs.add("<block>");
            }

            return tabArgs;
        }

    }

}
