package com._14ercooper.worldeditor.operations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.EntryNode;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.core.StringNode;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;
import com._14ercooper.worldeditor.operations.operators.world.BlockNode;

public class Parser {
    // This starts as -1 since the first thing the parser does is increment it
    public int index = -1;
    public List<String> parts;

    // Store operators
    public Map<String, Node> operators = new HashMap<String, Node>();

    public boolean AddOperator(String name, Node node) {
	if (operators.containsKey(name)) {
	    return false;
	}
	operators.put(name, node);
	return true;
    }

    public Node GetOperator(String name) {
	if (!operators.containsKey(name)) {
	    Main.logError("Operator \"" + name + "\" not found. Please check that you input a valid operator.",
		    Operator.currentPlayer, null);
	    return null;
	}
	return operators.get(name);
    }

    public EntryNode parseOperation(String op) {

	// Here there be parsing magic
	// A massive recursive nightmare
	index = -1;
	parts = Arrays.asList(Arrays.asList(op.split(" ")).stream().map(
		s -> s.matches(".*\\[.+=.+\\].*") ? s.replaceAll("[\\(\\)]+", "") : s.replaceAll("[\\(\\)\\[\\]]+", ""))
		.toArray(size -> new String[size]));

	Node rootNode = parsePart();

	// This is an error if this is true
	// Probably user error with an invalid operation
	if (rootNode == null) {
	    Main.logError("Operation parse failed. Please check your syntax.", Operator.currentPlayer, null);
	    return null;
	}

	// Generate the entry node of the operation
	Main.logDebug("Building entry node from root node"); // -----
	EntryNode entryNode = new EntryNode(rootNode);
	return entryNode;
    }
    
    public Node parsePart() {
	return parsePart(false);
    }

    // This is the massive recursive nightmare
    public Node parsePart(boolean numberNode) {
	index++;

	try {
	    // Comments
	    int commentTicker = -1;
	    if (parts.get(index).equalsIgnoreCase("/*")) {
		while (commentTicker > 0 || commentTicker == -1) {
		    if (commentTicker == -1) {
			commentTicker++;
		    }
		    if (parts.get(index).equalsIgnoreCase("/*")) {
			commentTicker++;
		    }
		    else if (parts.get(index).equalsIgnoreCase("*/")) {
			commentTicker--;
		    }
		    Main.logDebug("Skipped " + parts.get(index) + " with comment ticker " + commentTicker);
		    index++;
		}
	    }
	    
	    int oldIndex = index;

	    if (index == 0 && !operators.containsKey(parts.get(index))) {
		Main.logError(
			"First node parsed was a string node. This is likely a mistake. Please check that you used a valid operator.",
			Operator.currentPlayer, null);
	    }
	    if (operators.containsKey(parts.get(index))) {
		Node n = operators.get(parts.get(index)).newNode();
		Main.logDebug(parts.get(oldIndex) + " node created: " + n.toString());
		return n;
	    }
	    else {
		if (!numberNode) {
    		    index--;
    		    StringNode strNode = parseStringNode();
    		    BlockNode bn = new BlockNode().newNode(strNode.getText());
    		    if (bn != null) {
    			Main.logDebug("Block node created: " + bn.toString());
    			return bn;
    		    }
    		    else {
    			Main.logDebug("String node created: " + strNode.toString()); // -----
    			return strNode;
    		    }
		}
		else {
		    return new NumberNode().newNode();
		}
	    }
	}
	catch (IndexOutOfBoundsException e) {
	    return null;
	}
    }

    public NumberNode parseNumberNode() {
	Main.logDebug("Number node created"); // -----
	try {
	    return (NumberNode) this.parsePart(true);
	}
	catch (Exception e) {
	    Main.logError("Number expected. Did not find a number.", Operator.currentPlayer, e);
	    return null;
	}
    }

    public RangeNode parseRangeNode() {
	index++;
	Main.logDebug("Range node created"); // -----
	try {
	    return new RangeNode().newNode();
	}
	catch (Exception e) {
	    Main.logError("Range node expected. Could not create a range node.", Operator.currentPlayer, e);
	    return null;
	}
    }

    public StringNode parseStringNode() {
	index++;
	Main.logDebug("String node created"); // -----
	try {
	    StringNode node = new StringNode();
	    node.contents = parts.get(index);
	    Main.logDebug(node.contents);
	    return node;
	}
	catch (Exception e) {
	    Main.logError("Ran off end of operator (could not create string node). Are you missing arguments?",
		    Operator.currentPlayer, e);
	    return null;
	}
    }
}
