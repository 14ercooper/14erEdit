package fourteener.worldeditor.operations.operators;

import java.util.Random;

public class OddsNode extends Node {
	
	public NumberNode arg1;
	
	public static OddsNode newNode (NumberNode odds) {
		OddsNode oddsNode = new OddsNode();
		oddsNode.arg1 = odds;
		return oddsNode;
	}
	
	public boolean performNode () {
		Random rand = new Random();
		double chance = rand.nextDouble() * 100.0;
		return (chance < arg1.getValue());
	}
	
	public static int getArgCount () {
		return 3;
	}
}
