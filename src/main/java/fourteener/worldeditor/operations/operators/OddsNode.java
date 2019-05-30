package fourteener.worldeditor.operations.operators;

import java.util.Random;

public class OddsNode extends Node {
	
	public NumberNode arg;
	
	public static OddsNode newNode (NumberNode odds) {
		OddsNode oddsNode = new OddsNode();
		oddsNode.arg = odds;
		return oddsNode;
	}
	
	public boolean performNode () {
		Random rand = new Random();
		double chance = rand.nextDouble() * 100.0;
		return (chance < arg.getValue());
	}
	
	public static int getArgCount () {
		return 1;
	}
}
