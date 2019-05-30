package fourteener.worldeditor.operations.operators;

public class EntryNode {
	public Node node = null;
	
	public static EntryNode createEntryNode (Node newNode) {
		EntryNode entryNode = new EntryNode();
		entryNode.node = newNode;
		return entryNode;
	}
	
	public boolean performNode () {
		return node.performNode();
	}
}
