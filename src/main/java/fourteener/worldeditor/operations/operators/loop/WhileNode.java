package fourteener.worldeditor.operations.operators.loop;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.operators.Node;

public class WhileNode extends Node {
	
	Node cond, op;
	
	public WhileNode newNode() {
		WhileNode node = new WhileNode();
		node.cond = Main.operationParser.parsePart();
		node.op = Main.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		boolean result = true;
		while (cond.performNode()) {
			boolean result2 = op.performNode();
			result = result && result2;
		}
		return result;
	}
	
	public int getArgCount () {
		return 2;
	}
}
