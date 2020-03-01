package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;

public class DeallocNode {
	
	String type, name;
	
	public static DeallocNode newNode (String varType, String varName) {
		DeallocNode node = new DeallocNode();
		node.type = varType;
		node.name = varName;
		return node;
	}
	
	public boolean performNode () {
		if (type.equalsIgnoreCase("num")) {
			Operator.numericVars.remove(name);
			return true;
		}
		if (type.equalsIgnoreCase("itm")) {
			Operator.itemVars.remove(name);
			return true;
		}
		if (type.equalsIgnoreCase("blk")) {
			Operator.blockVars.remove(name);
			return true;
		}
		if (type.equalsIgnoreCase("mob")) {
			Operator.monsterVars.remove(name);
			return true;
		}
		if (type.equalsIgnoreCase("spwn")) {
			Operator.spawnerVars.remove(name);
			return true;
		}
		if (type.equalsIgnoreCase("file")) {
			Operator.fileLoads.remove(name);
			return true;
		}
		return false;
	}
	
	public static int getArgCount () {
		return 2;
	}
}
