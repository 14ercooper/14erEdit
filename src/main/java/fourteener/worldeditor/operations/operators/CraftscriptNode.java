package fourteener.worldeditor.operations.operators;

import java.util.Arrays;
import java.util.LinkedList;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;

public class CraftscriptNode extends Node {
	
	public String arg;
	
	public static CraftscriptNode newNode (String script) {
		CraftscriptNode csNode = new CraftscriptNode();
		csNode.arg = script;
		return csNode;
	}
	
	public boolean performNode () {
		String label = arg.split("{")[0];
		LinkedList<String> args = new LinkedList<String>(Arrays.asList(arg.split("{")[1].replace("}", "").split(",")));
		return Main.scriptManager.runCraftscript(label, args, Operator.currentPlayer);
	}
	
	public static int getArgCount () {
		return 1;
	}
}
