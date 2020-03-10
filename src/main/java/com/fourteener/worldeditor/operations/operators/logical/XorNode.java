package com.fourteener.worldeditor.operations.operators.logical;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.operators.Node;

public class XorNode extends Node {
	
	public Node arg1, arg2;
	
	public XorNode newNode() {
		XorNode node = new XorNode();
		node.arg1 = GlobalVars.operationParser.parsePart();
		node.arg2 = GlobalVars.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		boolean x = arg1.performNode();
		boolean y = arg2.performNode();
		return ((x || y) && !(x && y));
	}
	
	public int getArgCount () {
		return 2;
	}
}
