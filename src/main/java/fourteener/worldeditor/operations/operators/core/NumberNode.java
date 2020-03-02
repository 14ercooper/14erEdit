package fourteener.worldeditor.operations.operators.core;

import fourteener.worldeditor.operations.operators.Node;

public class NumberNode extends Node {

	// Stores this node's argument
	public double arg;
	
	// Create a new number node
	public NumberNode(String value) {
		arg = Double.parseDouble(value);
	}
	
	// Return the number
	public double getValue () {
		return arg;
	}
	
	// Get how many arguments this type of node takes
	public static int getArgCount () {
		return 1;
	}
}
