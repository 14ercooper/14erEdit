package com.fourteener.worldeditor.operations.operators.function;

import java.util.Random;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

public class OddsNode extends Node {
	
	public NumberNode arg;
	
	public OddsNode newNode() {
		OddsNode node = new OddsNode();
		node.arg = GlobalVars.operationParser.parseNumberNode();
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
