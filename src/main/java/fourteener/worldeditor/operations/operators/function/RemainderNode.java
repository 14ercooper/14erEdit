package fourteener.worldeditor.operations.operators.function;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

public class RemainderNode extends Node {
	
	public int arg1;
	public NumberNode arg2;
	
	public RemainderNode(String dim, NumberNode base) {
		if (dim.equalsIgnoreCase("x")) {
			arg1 = 0;
		}
		else if (dim.equalsIgnoreCase("y")) {
			arg1 = 1;
		}
		else if (dim.equalsIgnoreCase("z")) {
			arg1 = 2;
		}
		arg2 = base;
	}
	
	public boolean performNode () {
		int base = (int) arg2.getValue();
		int modBase = base * 2;
		if (arg1 == 0) {
			int value = Operator.currentBlock.getX();
			return Math.floorMod(value, modBase) < base;
		}
		else if (arg1 == 1) {
			int value = Operator.currentBlock.getY();
			return Math.floorMod(value, modBase) < base;
		}
		else if (arg1 == 2) {
			int value =  Operator.currentBlock.getZ();
			return Math.floorMod(value, modBase) < base;
		}
		return false;
	}
	
	public static int getArgCount () {
		return 2;
	}
}
