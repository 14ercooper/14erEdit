package com.fourteener.worldeditor.operations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.*;
import com.fourteener.worldeditor.operations.operators.function.*;
import com.fourteener.worldeditor.operations.operators.world.*;

public class Parser {
	// This starts as -1 since the first thing the parser does is increment it
	int index = -1;
	List<String> parts;
	
	// Store operators
	Map<String,Node> operators = new HashMap<String,Node>();
	
	public boolean AddOperator(String name, Node node) {
		if (operators.containsKey(name)) {
			return false;
		}
		operators.put(name, node);
		return true;
	}
	
	public EntryNode parseOperation (String op) {
		
		// Here there be parsing magic
		// A massive recursive nightmare
		index = -1;
		parts = Arrays.asList(op.split(" "));
		Node rootNode = parsePart();
		
		// This is an error if this is true
		// Probably user error with an invalid operation
		if (rootNode == null)
			return null;
		
		// Generate the entry node of the operation
		Main.logDebug("Building entry node from root node"); // -----
		EntryNode entryNode = new EntryNode(rootNode);
		return entryNode;
	}
	
	// This is the massive recursive nightmare
	public Node parsePart () {
		index++;
		
		if (operators.containsKey(parts.get(index))) {
			Main.logDebug(parts.get(index) + " node created");
			return operators.get(parts.get(index)).newNode();
		}
		else if (Material.matchMaterial(parts.get(index)) != null) {
			index--;
			Main.logDebug("Block node created, type " + Material.matchMaterial(parts.get(index+1)).name()); // -----
			return new BlockNode().newNode();
		}
		else if (Material.matchMaterial(parts.get(index).split("\\[")[0]) != null && parts.get(index).split("\\[").length > 1) {
			index--;
			Main.logDebug("Block node with data created, type " + Material.matchMaterial(parts.get(index+1).split("\\[")[0]).name()); // -----
			return new BlockNode().newNode(true);
		}
		else {
			Main.logDebug("Null node created"); // -----
			return null;
		}
	}
	
	public NumberNode parseNumberNode () {
		index ++;
		Main.logDebug("Number node created"); // -----
		return new NumberNode().newNode();
	}
	
	public RangeNode parseRangeNode () {
		index ++;
		Main.logDebug("Range node created"); // -----
		return new RangeNode().newNode();
	}
	
	public String parseStringNode () {
		index ++;
		Main.logDebug("String node created"); // -----
		return parts.get(index);
	}
}
