package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.undo.UndoSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.undo.UndoManager;

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
            return UndoSystem.findUserUndo(sender).undoChanges(numToRedo);
        }
        return false;
    }
}
