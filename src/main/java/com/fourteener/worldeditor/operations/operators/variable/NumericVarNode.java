package com.fourteener.worldeditor.operations.operators.variable;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.type.NumericVar;

public class NumericVarNode extends Node{
	
	String name;
	
	public NumericVarNode newNode() {
		NumericVarNode node = new NumericVarNode();
		node.name = GlobalVars.operationParser.parseStringNode().contents;
		return node;
	}
	
	public boolean performNode () {
		if (Operator.numericVars.containsKey(name)) {
			return false;
		}
		Operator.numericVars.put(name, new NumericVar());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
