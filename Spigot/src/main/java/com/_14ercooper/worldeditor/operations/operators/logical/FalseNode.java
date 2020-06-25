package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.operations.operators.Node;

public class FalseNode extends Node {
	
	public FalseNode newNode() {
		return new FalseNode();
	}
	
	public boolean performNode () {
		return false;
	}
	
	public int getArgCount () {
		return 0;
	}
}
