package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.operations.operators.Node;

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
