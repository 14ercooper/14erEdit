package fourteener.worldeditor.operations.operators.core;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.operators.Node;

public class LinkerNode extends Node {
	
	public Node arg1, arg2;
	
	public LinkerNode newNode() {
		LinkerNode node = new LinkerNode();
		node.arg1 = Main.operationParser.parsePart();
		node.arg2 = Main.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		boolean a1 = arg1.performNode();
		boolean a2 = arg2.performNode();
		return (a1 && a2);
	}
	
	public int getArgCount () {
		return 2;
	}
}
