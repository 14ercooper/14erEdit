package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandTemplate implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if (arg0 instanceof Player) {
            if (!arg0.isOp()) {
                arg0.sendMessage("You must be opped to use 14erEdit");
                return false;
            }
        }

        // Grab the filename
        String filename;
        try {
            filename = "plugins/14erEdit/templates/" + arg3[0];
        } catch (Exception e) {
            Main.logError("Template name required to use this command.", arg0, e);
            return false;
        }

        // Extension expansion
        if (Files.exists(Paths.get(filename))) {
            // Filename is good, keep it
        } else if (Files.exists(Paths.get(filename + ".txt"))) {
            filename += ".txt";
        } else if (Files.exists(Paths.get(filename + ".fx"))) {
            filename += ".fx";
        } else {
            Main.logError("Template not found.", arg0, null);
            return false;
        }

        // Grab the command
        String command;
        try {
            command = readFile(filename);
        } catch (IOException e) {
            Main.logError("Error reading template file.", arg0, e);
            return false;
        }

        // Clean up newlines
        command = command.replaceAll("[\\n\\r\\t ]+", " ");

        // Fill in template
        for (int i = arg3.length - 1; i >= 1; i--) {
            command = command.replaceAll("\\$" + i, arg3[i]);
        }

        Main.logDebug("Template command: " + command);

        // Run the command
        try {
            return Bukkit.dispatchCommand(arg0, command);
        } catch (Exception e) {
            Main.logError("Could not run command in template.", arg0, e);
            return false;
        }
    }

    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
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

    public static class TabComplete implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            List<String> tabArgs = new ArrayList<>();

            if (args.length == 1) {
                tabArgs.addAll(CommandTemplate.getTemplateList());
            }
            if (args.length > 1) {
                tabArgs.add("[template_arg_" + (args.length - 2) + "]");
            }

            return tabArgs;
        }
    }
}
