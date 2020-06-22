package com.fourteener.worldeditor.operations.operators.function;

import java.util.Random;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

public class OddsNode extends Node {

	public NumberNode arg;

	public OddsNode newNode() {
		OddsNode node = new OddsNode();
		try {
			node.arg = GlobalVars.operationParser.parseNumberNode();
		} catch (Exception e) {
			Main.logError("Could not create odds node, argument is not a number.", Operator.currentPlayer);
			return null;
		}
		if (node.arg == null) {
			Main.logError("Error creating odds node. Requires a number, but no number was found.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		Random rand = new Random();
		double chance = rand.nextDouble() * 100.0;
		return (chance < arg.getValue());
	}

	public int getArgCount () {
		return 1;
	}
}
