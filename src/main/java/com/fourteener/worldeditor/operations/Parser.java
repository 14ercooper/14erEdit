package com.fourteener.worldeditor.operations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.*;
import com.fourteener.worldeditor.operations.operators.function.*;
import com.fourteener.worldeditor.operations.operators.world.BlockNode;

public class Parser {
	// This starts as -1 since the first thing the parser does is increment it
	public int index = -1;
	public List<String> parts;

	// Store operators
	Map<String,Node> operators = new HashMap<String,Node>();

	public boolean AddOperator(String name, Node node) {
		if (operators.containsKey(name)) {
			return false;
		}
		operators.put(name, node);
		return true;
	}

	public Node GetOperator(String name) {
		if (!operators.containsKey(name)) return null;
		return operators.get(name);
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

		try {
			if (operators.containsKey(parts.get(index))) {
				Main.logDebug(parts.get(index) + " node created");
				return operators.get(parts.get(index)).newNode();
			}
			else {
				index--;
				StringNode strNode = parseStringNode();
				BlockNode bn = new BlockNode().newNode(strNode.getText());
				if (bn != null) {
					Main.logDebug("Block node created");
					return bn;
				}
				else {
					Main.logDebug("String node created"); // -----
					return strNode;
				}
			}
		} catch (IndexOutOfBoundsException e) {
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

	public StringNode parseStringNode () {
		index ++;
		Main.logDebug("String node created"); // -----
		StringNode node = new StringNode();
		node.contents = parts.get(index);
		return node;
	}
}
