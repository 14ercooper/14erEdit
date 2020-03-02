package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.BlockVar;

public class BlockVarNode extends Node{
	
	String name;
	
	public static BlockVarNode newNode (String varName) {
		BlockVarNode node = new BlockVarNode();
		node.name = varName;
		return node;
	}
	
	public boolean performNode () {
		if (Operator.blockVars.containsKey(name)) {
			return false;
		}
		Operator.blockVars.put(name, new BlockVar());
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
