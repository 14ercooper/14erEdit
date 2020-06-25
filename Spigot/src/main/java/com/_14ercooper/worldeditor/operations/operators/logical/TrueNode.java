package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.operations.operators.Node;

public class TrueNode extends Node{
	
	public TrueNode newNode() {
		return new TrueNode();
	}
	
	public boolean performNode () {
		return true;
	}
	
	public int getArgCount () {
		return 0;
	}
}
