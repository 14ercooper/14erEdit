package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.function.NoiseNode;

public class NumberNode extends Node {

    // Stores this node's argument
    public double arg = 0;

    // This was a randrange
    boolean isRange = false;
    NumberNode rangeMin;
    NumberNode rangeMax;

    // This was a noiserange
    boolean isNoise = false;
    NoiseNode noise;

    // Is absolute
    public boolean isAbsolute = false;

    // Create a new number node
    @Override
    public NumberNode newNode() {
	NumberNode node = new NumberNode();
	GlobalVars.operationParser.index--;
	String num = "undefined";
	try {
	    num = GlobalVars.operationParser.parseStringNode().contents;

	    if (num.equalsIgnoreCase("%-") || num.equalsIgnoreCase("randrange")) {
		node.rangeMin = GlobalVars.operationParser.parseNumberNode();
		node.rangeMax = GlobalVars.operationParser.parseNumberNode();
		node.isRange = true;

		return node;
	    }

	    if (num.equalsIgnoreCase("#-") || num.equalsIgnoreCase("randnoise")) {
		node.rangeMin = GlobalVars.operationParser.parseNumberNode();
		node.rangeMax = GlobalVars.operationParser.parseNumberNode();
		node.isNoise = true;

		return node;
	    }

	    if (num.toLowerCase().contains("a")) {
		node.isAbsolute = true;
		node.arg = Double.parseDouble(num.replaceAll("[A-Za-z]+", ""));
		return node;
	    }

	    node.arg = Double.parseDouble(num);
	    return node;
	}
	catch (Exception e) {
	    Main.logError("Could not parse number node. " + num + " is not a number.", Operator.currentPlayer, e);
	    return null;
	}
    }

    @Override
    public boolean performNode() {
	return Math.abs(arg) < 0.01 ? false : true;
    }

    // Return the number
    public double getValue() {
	return getValue(0);
    }

    public double getValue(double center) {
	if (isRange) {
//	    Main.logDebug("Returning from number node in range");
	    return (GlobalVars.rand.nextDouble() * (rangeMax.getValue() - rangeMin.getValue())) + rangeMin.getValue()
		    + center;
	}
	else if (isNoise) {
//	    Main.logDebug("Returning from number node using noise");
	    return (noise.getNum()) + center;
	}
	else {
//	    Main.logDebug("Returning from number node using value");
	    return arg;
	}
    }

    // Return the number as an int
    public int getInt() {
	return getInt(0);
    }

    public int getInt(int center) {
	return (int) getValue(center);
    }

    public int getMaxInt() {
	if (isRange || isNoise) {
	    return (int) rangeMax.getValue();
	}
	else {
	    return (int) arg;
	}
    }

    // Get how many arguments this type of node takes
    @Override
    public int getArgCount() {
	return 1;
    }

}
