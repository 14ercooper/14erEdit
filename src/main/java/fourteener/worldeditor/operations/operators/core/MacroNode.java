package fourteener.worldeditor.operations.operators.core;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class MacroNode extends Node {
	
	String arg;
	
	public MacroNode newNode() {
		MacroNode node = new MacroNode();
		node.arg = Main.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		Main.logDebug("Performing macro node"); // ----
		return Main.macroLauncher.launchMacro(arg, Operator.currentBlock.getLocation());
	}
	
	public int getArgCount () {
		return 1;
	}
}
