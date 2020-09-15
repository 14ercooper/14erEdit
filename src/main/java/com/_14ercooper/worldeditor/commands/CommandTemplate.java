package com._14ercooper.worldeditor.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com._14ercooper.worldeditor.main.Main;

public class CommandTemplate implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {	
	CommandSender player = arg0;
	
	// Grab the filename
	String filename;
	try {
	    filename = "plugins/14erEdit/templates/" + arg3[0];
	} catch (Exception e) {
	    Main.logError("Template name required to use this command.", player);
	    return false;
	}
	
	// Extension expansion
	if (Files.exists(Paths.get(filename))) {
	    // Filename is good, keep it
	}
	else if (Files.exists(Paths.get(filename + ".txt"))) {
	    filename += ".txt";
	}
	else if (Files.exists(Paths.get(filename + ".fx"))) {
	    filename += ".fx";
	}
	else {
	    Main.logError("Template not found.", player);
	    return false;
	}
	
	// Grab the command
	String command;
	try {
	    command = Files.readString(Paths.get(filename));
	}
	catch (IOException e) {
	    Main.logError("Error reading template file.", player);
	    return false;
	}
	
	// Clean up newlines
	command = command.replaceAll("[\\n\\r]+", " ");
	
	// Fill in template
	for (int i = 1; i < arg3.length; i++) {
	    command = command.replaceAll("\\$" + i, arg3[i]);
	}
	
	Main.logDebug("Template command: " + command);
	
	// Run the command
	try {
	    return Bukkit.dispatchCommand(player, command);
	}
	catch (Exception e) {
	    Main.logError("Could not run command in template.", player);
	    return false;
	}
    }

}
