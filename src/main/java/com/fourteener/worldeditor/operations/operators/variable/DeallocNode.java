package com.fourteener.worldeditor.operations.operators.variable;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class DeallocNode extends Node {
	
	String type, name;
	
	public DeallocNode newNode() {
		DeallocNode node = new DeallocNode();
		node.type = GlobalVars.operationParser.parseStringNode().contents;
		node.name = GlobalVars.operationParser.parseStringNode().contents;
		return node;
	}
	
	public boolean performNode () {
		if (type.equalsIgnoreCase("num")) {
			Operator.numericVars.remove(name);
			return true;
		}
		if (type.equalsIgnoreCase("itm")) {
			Operator.itemVars.remove(name);
			return true;
		}
		if (type.equalsIgnoreCase("blk")) {
			Operator.blockVars.remove(name);
			return true;
		}
		if (type.equalsIgnoreCase("mob")) {
			Operator.monsterVars.remove(name);
			return true;
		}
		if (type.equalsIgnoreCase("spwn")) {
			Operator.spawnerVars.remove(name);
			return true;
		}
		if (type.equalsIgnoreCase("file")) {
			Operator.fileLoads.remove(name);
			return true;
		}
		return false;
	}
	
	public int getArgCount () {
		return 2;
	}
}
