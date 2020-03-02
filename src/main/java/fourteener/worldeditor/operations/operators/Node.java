package fourteener.worldeditor.operations.operators;

public abstract class Node {
	
	public Node() {
		
	}
	
	public boolean performNode () {
		return false;
	}
	
	public static int getArgCount () {
		return -1;
	}
}
