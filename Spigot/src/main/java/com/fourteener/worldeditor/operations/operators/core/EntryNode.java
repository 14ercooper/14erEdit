package com.fourteener.worldeditor.operations.operators.core;

import com.fourteener.worldeditor.operations.operators.Node;

public class EntryNode {
	public Node node = null;
	
	public EntryNode (Node newNode) {
		node = newNode;
	}
	
	public boolean performNode () {
		return node.performNode();
	}
}
