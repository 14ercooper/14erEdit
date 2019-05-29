package fourteener.worldeditor.operations.operators;


public class NumberNode extends Node {

	// Stores this node's argument
	public double arg;
	
	// Create a new number node
	public static NumberNode newNode (String value) {
		NumberNode numberNode = new NumberNode();
		numberNode.arg = Double.parseDouble(value);
		return numberNode;
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
