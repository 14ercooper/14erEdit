package com.fourteener.worldeditor.operations.operators.function;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

public class RemainderNode extends Node {

	public int arg1 = -1;
	public NumberNode arg2;

	public RemainderNode newNode() {
		RemainderNode node = new RemainderNode();
		try {
			String dim = GlobalVars.operationParser.parseStringNode().contents;
			if (dim.equalsIgnoreCase("x")) {
				node.arg1 = 0;
			}
			else if (dim.equalsIgnoreCase("y")) {
				node.arg1 = 1;
			}
			else if (dim.equalsIgnoreCase("z")) {
				node.arg1 = 2;
			}
			arg2 = GlobalVars.operationParser.parseNumberNode();
		} catch (Exception e) {
			Main.logError("Could not create remainder node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.arg2 == null) {
			Main.logError("Could not create remainder node. Requires an axis and a number, but these were not given.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		int base = (int) arg2.getValue();
		int modBase = base * 2;
		if (arg1 == 0) {
			int value = Operator.currentBlock.getX();
			return Math.floorMod(value, modBase) < base;
		}
		else if (arg1 == 1) {
			int value = Operator.currentBlock.getY();
			return Math.floorMod(value, modBase) < base;
		}
		else if (arg1 == 2) {
			int value =  Operator.currentBlock.getZ();
			return Math.floorMod(value, modBase) < base;
		}
		Main.logError("Invalid axis provided to remainder node. Please check your syntax.", Operator.currentPlayer);
		return false;
	}

	public int getArgCount () {
		return 2;
	}
}
