package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.type.ItemVar;

public class GetItemVarNode extends Node {
	
	String name;
	
	public static GetItemVarNode newNode (String val) {
		GetItemVarNode node =  new GetItemVarNode();
		node.name = val;
		return node;
	}
	
	public boolean performNode () {
		ItemVar iv = Operator.itemVars.get(name);
		String command = "give @s ";
		command += iv.getType().toLowerCase();
		command += iv.getNBT() + " ";
		command += iv.getCount();
		Main.logDebug("Command: " + command);
		Operator.currentPlayer.performCommand(command);
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
