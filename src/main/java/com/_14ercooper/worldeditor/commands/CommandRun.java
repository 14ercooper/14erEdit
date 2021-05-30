package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorLoader;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// These are dedicated versions of the undo and redo commands
public class CommandRun implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.isOp()) {
                sender.sendMessage("You must be opped to use 14erEdit");
                return false;
            }
        }

        try {
            if (sender instanceof Player) {
                String opStr = "";
                for (String s : args) {
                    opStr = opStr.concat(s).concat(" ");
                }
                Operator op = new Operator(opStr, (Player) sender);
                Block b = ((Player) sender).getWorld().getBlockAt(((Player) sender).getLocation());
                op.operateOnBlock(b, (Player) sender);
                return true;
            }
            Main.logError("This must be run as a player.", sender, null);
            return false;
        } catch (Exception e) {
            Main.logError("Error performing run command. Please check your syntax.", sender, e);
            return false;
        }
    }

    public static class TabComplete implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            List<String> tabArgs = new ArrayList<>();

            int initOffset = 2;
            if (args.length < initOffset) {
                tabArgs.addAll(OperatorLoader.nodeNames);
            }
            else {
                String lastArg = args[args.length - initOffset];
                if (OperatorLoader.nextRange.contains(lastArg)) {
                    tabArgs.addAll(OperatorLoader.numberNodeNames);
                }
                else if (args.length > initOffset && OperatorLoader.nextRange.contains(args[args.length-initOffset-1])) {
                    tabArgs.addAll(OperatorLoader.numberNodeNames);
                }
                else if (OperatorLoader.nextBlock.contains(lastArg)) {
                    tabArgs.addAll(OperatorLoader.blockNodeNames);
                    tabArgs.add("<block_name>");
                }
                else {
                    tabArgs.addAll(OperatorLoader.nodeNames);
                    tabArgs.add("<block_name>");
                }
            }

            return tabArgs;
        }
    }
}
