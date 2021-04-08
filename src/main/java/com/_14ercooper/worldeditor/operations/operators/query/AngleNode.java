package com._14ercooper.worldeditor.operations.operators.query;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;

public class AngleNode extends Node {

    RangeNode angleForTrue = null;
    NumberNode distance = null;

    @Override
    public Node newNode() {
	try {
	    AngleNode node = new AngleNode();
	    node.angleForTrue = GlobalVars.operationParser.parseRangeNode();
	    node.distance = GlobalVars.operationParser.parseNumberNode();
	    if (node.distance == null) {
		Main.logError("Could not parse angle node. Did you provide a range node and a distance?",
			Operator.currentPlayer, null);
		return null;
	    }
	    return node;
	}
	catch (Exception e) {
	    Main.logError("Error parsing range node. Please check your syntax.", Operator.currentPlayer, e);
	    return null;
	}
    }

    @Override
    public boolean performNode() {
	// Get angle from each block pair
	int dist = (int) distance.getValue();
	int maxAngle = getAngle(Operator.currentBlock.getRelative(dist, 0, 0),
		Operator.currentBlock.getRelative(-dist, 0, 0));
	int angle = getAngle(Operator.currentBlock.getRelative(0, 0, dist),
		Operator.currentBlock.getRelative(0, 0, -dist));
	if (angle > maxAngle)
	    maxAngle = angle;
	angle = getAngle(Operator.currentBlock.getRelative((int) (dist * 0.707), 0, (int) (dist * 0.707)),
		Operator.currentBlock.getRelative((int) (-dist * 0.707), 0, (int) (-dist * 0.707)));
	if (angle > maxAngle)
	    maxAngle = angle;
	angle = getAngle(Operator.currentBlock.getRelative((int) (dist * 0.707), 0, (int) (-dist * 0.707)),
		Operator.currentBlock.getRelative((int) (-dist * 0.707), 0, (int) (dist * 0.707)));
	if (angle > maxAngle)
	    maxAngle = angle;
	// Return if max angle found is in range
	return angleForTrue.getMin() <= maxAngle && angleForTrue.getMax() >= maxAngle;
    }

    @Override
    public int getArgCount() {
	return 2;
    }

    private int getAngle(Block b1, Block b2) {
	// Check current states both blocks
	Material mat1 = b1.getType();
	Material mat2 = b2.getType();
	// Both solid
	if (!GlobalVars.brushMask.contains(mat1) && !GlobalVars.brushMask.contains(mat2)) {
	    return 0;
	}
	// Neither solid
	else if (GlobalVars.brushMask.contains(mat1) && GlobalVars.brushMask.contains(mat1)) {
	    return 0;
	}
	// One solid
	else {
	    if (GlobalVars.brushMask.contains(mat2)) {
		Block temp = b2;
		b2 = b1;
		b1 = temp;
	    }
	    int downVal = 0;
	    while (GlobalVars.brushMask.contains(b1.getRelative(0, downVal, 0).getType())) {
		downVal--;
	    }
	    int upVal = 0;
	    while (!GlobalVars.brushMask.contains(b2.getRelative(0, upVal, 0).getType())) {
		upVal++;
	    }
	    double distVert = upVal - downVal;
	    double distHor = 2 * distance.getValue();
	    return (int) Math.abs(Math.atan2(distVert, distHor) * 57.296);
	}
    }
}
