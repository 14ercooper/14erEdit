package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class DeallocNode extends Node {
	
	String type, name;
	
	public DeallocNode(String varType, String varName) {
		type = varType;
		name = varName;
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
