package fourteener.worldeditor.operations.operators.core;

import java.util.Arrays;
import java.util.LinkedList;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class CraftscriptNode extends Node {
	
	public String arg;
	
	public CraftscriptNode(String script) {
		arg = script;
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
