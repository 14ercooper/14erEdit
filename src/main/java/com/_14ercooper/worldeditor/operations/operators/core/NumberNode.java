package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class NumberNode extends Node {

	// Stores this node's argument
	public double arg;
	
	// Create a new number node
	public NumberNode newNode() {
		NumberNode node = new NumberNode();
		GlobalVars.operationParser.index--;
		String num = "undefined";
		try {
			num = GlobalVars.operationParser.parseStringNode().contents;
			node.arg = Double.parseDouble(num);
			return node;
		} catch (Exception e) {
			Main.logError("Could not parse number node. " + num + " is not a number.", Operator.currentPlayer);
			return null;
		}
	}

	public boolean performNode() {
		return arg < 0.01 ? false : true;
	}
	
	// Return the number
	public double getValue () {
		return arg;
	}
	
	// Get how many arguments this type of node takes
	public int getArgCount () {
		return 1;
	}
	
}
