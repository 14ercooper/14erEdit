package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.ItemVar;

public class ItemVarNode extends Node{
	
	String name;
	
	public ItemVarNode newNode() {
		ItemVarNode node = new ItemVarNode();
		node.name = GlobalVars.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		if (Operator.itemVars.containsKey(name)) {
			return false;
		}
		Operator.itemVars.put(name, new ItemVar());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
