package com.fourteener.worldeditor.operations.operators.logical;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.operators.Node;

public class NotNode extends Node {
	public Node arg;
	
	public NotNode newNode() {
		NotNode node = new NotNode();
		node.arg = GlobalVars.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		return !(arg.performNode());
	}
	
	public int getArgCount () {
		return 1;
	}
}
