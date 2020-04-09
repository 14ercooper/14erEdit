package com.fourteener.worldeditor.operations.operators.core;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.operators.Node;

public class NumberNode extends Node {

	// Stores this node's argument
	public double arg;
	
	// Create a new number node
	public NumberNode newNode() {
		NumberNode node = new NumberNode();
		GlobalVars.operationParser.index--;
		node.arg = Double.parseDouble(GlobalVars.operationParser.parseStringNode().contents);
		return node;
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
