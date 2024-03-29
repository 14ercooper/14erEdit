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

import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorLoader;
import com._14ercooper.worldeditor.player.PlayerManager;
import com._14ercooper.worldeditor.selection.*;
import com._14ercooper.worldeditor.undo.UndoSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
                    return true;
                }

                // Reset a player
                else if (args[argOffset].equalsIgnoreCase("reset")) {
                    PlayerManager.INSTANCE.deletePlayerWrapper(((Player) sender).getUniqueId().toString());
                    return true;
                }

                // Multiselection stuff
                if (args[argOffset].equalsIgnoreCase("multi")) {
                    SelectionManager manager = SelectionManager.getSelectionManager(((Player) sender).getUniqueId());
                    if (args.length == argOffset + 1) {
                        manager.toggleMode(((Player) sender).getUniqueId().toString());
                        return true;
                    }
                    else if (args[argOffset + 1].equalsIgnoreCase("reset")) {
                        manager.resetMultiSelect(((Player) sender).getUniqueId().toString());
                        return true;
                    }
                    else if (args[argOffset + 1].equalsIgnoreCase("remove")) {
                        manager.removeAdditionalPoint(((Player) sender).getUniqueId().toString());
                        return true;
                    }
                    else {
                        return MultiselectCommandHandler.handleCommand(manager, ((Player) sender).getUniqueId().toString(), args);
                    }
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
                        new Brush(args, argOffset,
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
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getSchematicsList() {
        try {
            Stream<Path> files = Files.list(Paths.get("plugins/14erEdit/schematics"));
            List<String> functions = files.map(path -> path.getFileName().toString()).collect(Collectors.toList());
            files.close();
            return functions;
        } catch (IOException e) {
            return new ArrayList<>(Collections.singleton("<schematic_name>"));
        }
    }

    public static class TabComplete implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            Set<String> tabArgs = new HashSet<>();

            int initOffset = 2;

            if (args.length < 2) {
                tabArgs.add("brush");
                tabArgs.add("br");
                tabArgs.add("wand");
                tabArgs.add("selection");
                tabArgs.add("sel");
                tabArgs.add("schem");
                tabArgs.add("reset");
                tabArgs.add("multi");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("brush") || args[0].equalsIgnoreCase("br")) {
                    tabArgs.add("none");
                    tabArgs.addAll(Brush.brushShapes.keySet());
                }
                if (args[0].equalsIgnoreCase("selection") || args[0].equalsIgnoreCase("sel")) {
                    tabArgs.add("op");
                    tabArgs.add("expand");
                    tabArgs.add("reset");
                    tabArgs.add("copy");
                    tabArgs.add("paste");
                    tabArgs.add("origin");
                    tabArgs.add("pos1");
                    tabArgs.add("pos2");
                    tabArgs.add("mirror");
                    tabArgs.add("rotate");
                    tabArgs.add("clone");
                }
                if (args[0].equalsIgnoreCase("schem")) {
                    tabArgs.add("save");
                    tabArgs.add("load");
                    tabArgs.add("list");
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("schem")) {
                if (args[1].equalsIgnoreCase("load")) {
                    tabArgs.addAll(getSchematicsList());
                }
                if (args[1].equalsIgnoreCase("save")) {
                    tabArgs.add("<schematic_name>");
                }
                if (args[1].equalsIgnoreCase("list")) {
                    tabArgs.add("[regex]");
                }
            } else if ((args[0].equalsIgnoreCase("brush") || args[0].equalsIgnoreCase("br")) && Brush.GetBrushShape(args[1]) == null) {
                tabArgs.add("invalid_brush");
            } else if ((args[0].equalsIgnoreCase("brush") || args[0].equalsIgnoreCase("br")) && args.length < 3 + Brush.GetBrushShape(args[1]).minArgCount()) {
                tabArgs.add("<brush_arg>");
            } else if ((args[0].equalsIgnoreCase("selection") || args[0].equalsIgnoreCase("sel")) && !(args[1].equalsIgnoreCase("op"))) {
                if (args[1].equalsIgnoreCase("expand")) {
                    if (args.length == 3) {
                        tabArgs.add("<amt>");
                    } else if (args.length == 4) {
                        tabArgs.add("<direction>");
                    }
                } else if (args[1].equalsIgnoreCase("paste")) {
                    tabArgs.add("true");
                    tabArgs.add("false");
                } else if (args[1].equalsIgnoreCase("origin")) {
                    if (args.length == 3) {
                        tabArgs.add("shift");
                        tabArgs.add("set");
                    } else if (args.length == 4) {
                        tabArgs.add("<x>");
                    } else if (args.length == 5) {
                        tabArgs.add("<y>");
                    } else if (args.length == 6) {
                        tabArgs.add("<z>");
                    }
                } else if (args[1].equalsIgnoreCase("pos1")) {
                    if (args.length == 3) {
                        tabArgs.add("<x>");
                    } else if (args.length == 4) {
                        tabArgs.add("<y>");
                    } else if (args.length == 5) {
                        tabArgs.add("<z>");
                    }
                } else if (args[1].equalsIgnoreCase("pos2")) {
                    if (args.length == 3) {
                        tabArgs.add("<x>");
                    } else if (args.length == 4) {
                        tabArgs.add("<y>");
                    } else if (args.length == 5) {
                        tabArgs.add("<z>");
                    }
                } else if (args[1].equalsIgnoreCase("mirror")) {
                    if (args.length == 3) {
                        tabArgs.add("x");
                        tabArgs.add("y");
                        tabArgs.add("z");
                    }
                } else if (args[1].equalsIgnoreCase("rotate")) {
                    if (args.length == 3) {
                        tabArgs.add("0");
                        tabArgs.add("1");
                        tabArgs.add("2");
                        tabArgs.add("3");
                        tabArgs.add("4");
                        tabArgs.add("5");
                    }
                } else if (args[1].equalsIgnoreCase("clone")) {
                    if (args.length == 3) {
                        tabArgs.add("<xOffset>");
                    }
                    if (args.length == 4) {
                        tabArgs.add("<yOffset>");
                    }
                    if (args.length == 5) {
                        tabArgs.add("<zOffset>");
                    }
                    if (args.length == 6) {
                        tabArgs.add("<times>");
                    }
                    if (args.length == 7) {
                        tabArgs.add("false");
                        tabArgs.add("true");
                    }
                }
            } else {
                if (args[1].equalsIgnoreCase("selection") || args[1].equalsIgnoreCase("sel")) {
                    initOffset = 4;
                }
                if (args[1].equalsIgnoreCase("brush") || args[1].equalsIgnoreCase("br")) {
                    initOffset = 3 + Brush.GetBrushShape(args[3]).minArgCount();
                }
                if (args[1].equalsIgnoreCase("multi")) {
                    initOffset = 3;
                }
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

            if (args.length >= 2) {
                if (args[args.length - 2].equalsIgnoreCase("template") || args[args.length - 2].equalsIgnoreCase("tpl")) {
                    tabArgs = new HashSet<>(getTemplateList());
                }
                if (args[args.length - 2].equalsIgnoreCase("multibrush") || args[args.length - 2].equalsIgnoreCase("multi")) {
                    tabArgs = new HashSet<>(getMultibrushList());
                }
                if (args[args.length - 2].equalsIgnoreCase("brush") || args[args.length - 2].equalsIgnoreCase("br")) {
                    tabArgs = new HashSet<>();
                    if (args.length == 2) {
                        tabArgs.add("none");
                    }
                    tabArgs.addAll(Brush.brushShapes.keySet());
                }
            }

            if (args[0].equalsIgnoreCase("multi")) {
                if (args.length == 2) {
                    tabArgs.add("reset");
                    tabArgs.add("remove");
                    tabArgs.add("spline");
                    tabArgs.add("uniformspline");
                    tabArgs.add("chordalspline");
                }
            }

            return new ArrayList<>(tabArgs);
        }
    }

    public static List<String> getTemplateList() {
        try {
            Stream<Path> files = Files.list(Paths.get("plugins/14erEdit/templates"));
            List<String> functions = files.map(path -> path.getFileName().toString()).collect(Collectors.toList());
            files.close();
            return functions;
        } catch (IOException e) {
            return new ArrayList<>(Collections.singleton("<template_name>"));
        }
    }

    public static List<String> getMultibrushList() {
        try {
            Stream<Path> files = Files.list(Paths.get("plugins/14erEdit/multibrushes"));
            List<String> functions = files.map(path -> path.getFileName().toString()).collect(Collectors.toList());
            files.close();
            return functions;
        } catch (IOException e) {
            return new ArrayList<>(Collections.singleton("<multibrush_name>"));
        }
    }
}
