package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.operations.operators.Node;

public class FalseNode extends Node {
	
	public FalseNode() {
		return;
	}
	
	public boolean performNode () {
		return false;
	}
	
	public static int getArgCount () {
		return 0;
	}
}
