package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.BlockVar;

public class BlockVarNode extends Node{
	
	String name;
	
	public BlockVarNode(String varName) {
		name = varName;
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
