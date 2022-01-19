// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


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
