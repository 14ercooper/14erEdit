package com._14ercooper.worldeditor.operations.operators.fun;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class TemplateNode extends Node {

    String filename;
    List<String> args = new ArrayList<String>();
    
    @Override
    public TemplateNode newNode() {
	TemplateNode node = new TemplateNode();
	node.filename = "plugins/14erEdit/templates/" + GlobalVars.operationParser.parseStringNode().getText();
	int argCount = (int) GlobalVars.operationParser.parseNumberNode().getValue();
	for (int i = 0; i < argCount; i++) {
	    node.args.add(GlobalVars.operationParser.parseStringNode().getText());
	}
	
	return node;
    }

    @Override
    public boolean performNode() {
	Player player = Operator.currentPlayer;
	
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
	command = command.replaceAll("\\s+", " ");
	
	// Fill in template
	for (int i = 0; i < args.size(); i++) {
	    command = command.replaceAll("\\$" + (i+1), args.get(i));
	}
	
	Main.logDebug("Template command: " + command);
	
	// Run the command
	try {
	    Location loc = player.getLocation();
	    player.teleport(Operator.currentBlock.getLocation());
	    boolean retVal = Bukkit.dispatchCommand(player, command);
	    player.teleport(loc);
	    return retVal;
	}
	catch (Exception e) {
	    Main.logError("Could not run command in template.", player);
	    return false;
	}
    }

    @Override
    public int getArgCount() {
	return 2;
    }

}
