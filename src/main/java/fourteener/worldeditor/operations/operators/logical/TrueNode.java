package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.operations.operators.Node;

public class TrueNode extends Node{
	
	public static TrueNode newNode () {
		return new TrueNode();
	}
	
	public boolean performNode () {
		return true;
	}
	
	public static int getArgCount () {
		return 0;
	}
}
