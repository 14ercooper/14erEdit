package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.BlockVar;

public class BlockVarNode extends Node{
	
	String name;
	
	public BlockVarNode newNode() {
		BlockVarNode node = new BlockVarNode();
		node.name = Main.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		if (Operator.blockVars.containsKey(name)) {
			return false;
		}
		Operator.blockVars.put(name, new BlockVar());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
