package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;

public class RemainderNode extends Node {
	
	public int arg1;
	public NumberNode arg2;
	
	public static RemainderNode newNode (String dim, NumberNode base) {
		RemainderNode rmNode = new RemainderNode();
		if (dim.equalsIgnoreCase("x")) {
			rmNode.arg1 = 0;
		}
		else if (dim.equalsIgnoreCase("y")) {
			rmNode.arg1 = 1;
		}
		else if (dim.equalsIgnoreCase("z")) {
			rmNode.arg1 = 2;
		}
		rmNode.arg2 = base;
		return rmNode;
	}
	
	public boolean performNode () {
		int base = (int) arg2.getValue();
		int modBase = base * 2;
		if (arg1 == 0) {
			int posVal = Operator.currentBlock.getX();
			return (posVal % modBase) < base;
		}
		else if (arg1 == 1) {
			int posVal = Operator.currentBlock.getY();
			return (posVal % modBase) < base;
		}
		else if (arg1 == 2) {
			int posVal = Operator.currentBlock.getZ();
			return (posVal % modBase) < base;
		}
		return false;
	}
	
	public static int getArgCount () {
		return 2;
	}
}
