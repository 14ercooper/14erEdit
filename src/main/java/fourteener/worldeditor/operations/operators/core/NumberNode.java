package fourteener.worldeditor.operations.operators.core;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.operators.Node;

public class NumberNode extends Node {

	// Stores this node's argument
	public double arg;
	
	// Create a new number node
	public NumberNode newNode() {
		NumberNode node = new NumberNode();
		node.arg = Double.parseDouble(GlobalVars.operationParser.parseStringNode());
		return node;
	}

	public boolean performNode() {
		return false;
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
