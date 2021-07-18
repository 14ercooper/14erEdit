package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandFunction implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender arg0, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] arg3) {
        if (arg0 instanceof Player) {
            if (!arg0.isOp()) {
                arg0.sendMessage("You must be opped to use 14erEdit");
                return false;
            }
        }

        if (!(arg0 instanceof Player)) {
            Main.logError("This command must be run as a player.", arg0, null);
            return false;
        }

        Player player = (Player) arg0;
        String filename;
        try {
            filename = arg3[0];
        } catch (Exception e) {
            Main.logError("Must provide at least a filename.", player, e);
            return false;
        }
        List<String> args = new ArrayList<>(Arrays.asList(arg3).subList(1, arg3.length));
        Function fx = new Function(filename, args, player, false, null);
        fx.run();
        return true;
    }

    public static List<String> getFunctionsList() {
        try {
            Stream<Path> files = Files.list(Paths.get("plugins/14erEdit/functions"));
            List<String> functions = files.map(path -> path.getFileName().toString()).collect(Collectors.toList());
            files.close();
            return functions;
        }
        catch (IOException e) {
            return new ArrayList<>(Collections.singleton("<function_name>"));
        }
    }

    public static class TabComplete implements TabCompleter {
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
            List<String> tabArgs = new ArrayList<>();

            if (args.length == 1) {
                tabArgs.addAll(CommandFunction.getFunctionsList());
            }
            if (args.length > 1) {
                tabArgs.add("[function_arg_" + (args.length-2) + "]");
            }

            return tabArgs;
        }
    }
}
