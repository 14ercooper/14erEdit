// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorLoader;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.undo.UndoElement;
import com._14ercooper.worldeditor.undo.UndoMode;
import com._14ercooper.worldeditor.undo.UndoSystem;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// These are dedicated versions of the undo and redo commands
public class CommandRun implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
                Operator op = new Operator(opStr, sender);
                Block b = ((Player) sender).getWorld().getBlockAt(((Player) sender).getLocation());
                UndoElement undoElement = UndoSystem.findUserUndo(sender).getNewUndoElement();
                op.operateOnBlock(new OperatorState(new BlockWrapper(b, b.getX(), b.getY(), b.getZ()), sender, ((Player) sender).getWorld(), undoElement, b.getLocation()));
                if (undoElement.getCurrentState() == UndoMode.WAITING_FOR_BLOCKS)
                    undoElement.getUserUndo().finalizeUndo(undoElement);
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
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
            List<String> tabArgs = new ArrayList<>();

            int initOffset = 2;
            if (args.length < initOffset) {
                tabArgs.addAll(OperatorLoader.nodeNames);
            } else {
                String lastArg = args[args.length - initOffset];
                if (OperatorLoader.nextRange.contains(lastArg)) {
                    tabArgs.addAll(OperatorLoader.rangeNodeNames);
                } else if (OperatorLoader.nextBlock.contains(lastArg)) {
                    tabArgs.addAll(OperatorLoader.blockNodeNames);
                    tabArgs.addAll(Main.getBlockNames(args[args.length - 1]));
                } else {
                    tabArgs.addAll(OperatorLoader.nodeNames);
                    tabArgs.addAll(Main.getBlockNames(args[args.length - 1]));
                }
            }

            return tabArgs;
        }
    }
}
