package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.selection.SchematicHandler;
import com._14ercooper.worldeditor.selection.SelectionCommand;
import com._14ercooper.worldeditor.selection.SelectionWand;
import com._14ercooper.worldeditor.selection.SelectionWandListener;
import com._14ercooper.worldeditor.undo.UndoSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// For the fx command
public class CommandFx implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.isOp()) {
                sender.sendMessage("You must be opped to use 14erEdit");
                return false;
            }
        }

        try {
            int argOffset = 0;
            if (args.length < argOffset + 1) {
                Main.logError("fx requires at least one argument.", sender, null);
                return false;
            }
            if (sender instanceof Player) {
                // Calls the wand command, giving the player a wand
                if (args[argOffset].equalsIgnoreCase("wand")) {
                    SelectionWand wand = (SelectionWand.giveNewWand(((Player) sender).getPlayer()));
                    if (SelectionWandListener.wands.contains(wand)) {
                    } else {
                        SelectionWandListener.wands.add(wand);
                    }
                    return true;
                }

                // Calls the brush command, handling the creation of a new brush
                else if (args[argOffset].equalsIgnoreCase("brush") || args[argOffset].equalsIgnoreCase("br")) {
                    // Unassign a brush if asked
                    if (args[argOffset + 1].equalsIgnoreCase("none")) {
                        sender.sendMessage("§dBrush removed.");
                        return Brush.removeBrush((Player) sender);
                    }
                    // Create a new brush as requested
                    else {
                        new Brush(args[argOffset + 1], args[argOffset + 2], args, argOffset,
                                (Player) sender);
                        return true;
                    }
                }

                // Calls the selection command, handling operating on selections
                else if (args[argOffset].equalsIgnoreCase("selection") || args[argOffset].equalsIgnoreCase("sel")) {
                    return SelectionCommand.performCommand(args, (Player) sender);
                }

                // Undo and redo commands
                else if (args[argOffset].equalsIgnoreCase("undo")) {
                    return UndoSystem.findUserUndo(sender).undoChanges(Integer.parseInt(args[argOffset + 1]));
                } else if (args[argOffset].equalsIgnoreCase("redo")) {
                    return UndoSystem.findUserUndo(sender).redoChanges(Integer.parseInt(args[argOffset + 1]));
                }

                // Save and load schematics
                else if (args[argOffset].equalsIgnoreCase("schem")) {
                    if (args[argOffset + 1].equalsIgnoreCase("save")) {
                        SchematicHandler.saveSchematic(args[argOffset + 2], (Player) sender);
                        return true;
                    } else if (args[argOffset + 1].equalsIgnoreCase("load")) {
                        SchematicHandler.loadSchematic(args[argOffset + 2], (Player) sender,
                                args.length > argOffset + 3 ? args[argOffset + 3] : "",
                                args.length > argOffset + 4 && Boolean.parseBoolean(args[argOffset + 4]),
                                args.length > argOffset + 5 ? Integer.parseInt(args[argOffset + 5]) : 0);
                        return true;
                    } else if (args[argOffset + 1].equalsIgnoreCase("list")) {
                        Stream<Path> files = Files.list(Paths.get("plugins/14erEdit/schematics"));
                        String regex = ".+";
                        if (args.length > argOffset + 2)
                            regex = args[argOffset + 2];
                        final String finalRegex = regex;
                        Set<String> filePaths = files.filter(file -> !Files.isDirectory(file)).map(Path::getFileName)
                                .map(Path::toString).filter(file -> file.matches(finalRegex))
                                .collect(Collectors.toSet());
                        long schemNum = filePaths.size();
                        StringBuilder filesString = new StringBuilder();
                        for (String s : filePaths) {
                            filesString.append(" ").append(s);
                        }
                        sender.sendMessage("§aFound " + schemNum + " schematics:" + filesString);
                        files.close();
                        return true;
                    } else if (args[argOffset + 1].equalsIgnoreCase("delete")) {
                        Main.logDebug("Deleting schematic " + args[argOffset + 2]);
                        Files.deleteIfExists(Paths.get("plugins/14erEdit/schematics/" + args[argOffset + 2]));
                        sender.sendMessage("§aDeleted schematic " + args[argOffset + 2]);
                        return true;
                    }
                }

                return false;
            }
            Main.logError("This command must be run as a player.", sender, null);
            return false;
        } catch (Exception e) {
            Main.logError("Error in fx command. Please check your syntax.", sender, e);
            return false;
        }
    }
}
