package fourteener.worldeditor.operations.operators;

import java.util.Random;

public class OddsNode extends Node {
	
	public NumberNode arg1;
	public Node arg2, arg3;
	
	public static OddsNode newNode (NumberNode odds, Node ifTrue, Node ifFalse) {
		OddsNode oddsNode = new OddsNode();
		oddsNode.arg1 = odds;
		oddsNode.arg2 = ifTrue;
		oddsNode.arg3 = ifFalse;
		return oddsNode;
	}
	
	public boolean performNode () {
		Random rand = new Random();
		double chance = rand.nextDouble() * 100.0;
		if (chance < arg1.getValue()) {
			return arg2.performNode();
		}
		else {
			return arg3.performNode();
		}
	}
	
	public static int getArgCount () {
		return 3;
	}
}
