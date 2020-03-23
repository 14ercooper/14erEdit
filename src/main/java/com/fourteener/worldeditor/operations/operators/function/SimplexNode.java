package com.fourteener.worldeditor.operations.operators.function;

import org.bukkit.Location;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

// This node currently accepts both 2 and 3 as dimensions using worldspace
// 4 dimensions uses worldspace plus average of the three other dimensions, creating good looking results
public class SimplexNode extends Node {
	
	public NumberNode arg1, arg2, scaleFactor;
	
	public SimplexNode newNode() {
		SimplexNode node = new SimplexNode();
		node.arg1 = GlobalVars.operationParser.parseNumberNode();
		node.arg2 = GlobalVars.operationParser.parseNumberNode();
		node.scaleFactor = GlobalVars.operationParser.parseNumberNode();
		return node;
	}
	
	public boolean performNode () {
		// The range on all of these are useful for double inaccuracy
		double scale = 4 * scaleFactor.getValue();
		if (arg1.getValue() <= 2.1 && arg1.getValue() >= 1.9) {
			Location loc = Operator.currentBlock.getLocation();
			return GlobalVars.simplexNoise.noise(loc.getX() / scale, loc.getZ() / scale) <= arg2.getValue();
		}
		if (arg1.getValue() <= 3.1 && arg1.getValue() >= 2.9) {
			Location loc = Operator.currentBlock.getLocation();
			return GlobalVars.simplexNoise.noise(loc.getX() / scale, loc.getY() / scale, loc.getZ() / scale) <= arg2.getValue();
		}
		if (arg1.getValue() <= 4.1 && arg1.getValue() >= 3.9) {
			Location loc = Operator.currentBlock.getLocation();
			return GlobalVars.simplexNoise.noise(loc.getX() / scale, loc.getY() / scale, loc.getZ() / scale, (loc.getX() + loc.getY() + loc.getZ()) * 0.33333333 / scale) <= arg2.getValue();
		}
		return false;
	}
	
	public int getArgCount () {
		return 2;
	}
}
