package com.fourteener.worldeditor.operations.operators.loop;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

public class NumericLessNode extends Node {
	
	public String name;
	public NumberNode val;
	
	public NumericLessNode newNode() {
		NumericLessNode node = new NumericLessNode();
		node.name = GlobalVars.operationParser.parseStringNode();
		node.val = GlobalVars.operationParser.parseNumberNode();
		return node;
	}
	
	public boolean performNode () {
		return (Operator.numericVars.get(name).getValue() < val.getValue());
	}
	
	public int getArgCount () {
		return 2;
	}
}
