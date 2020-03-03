package fourteener.worldeditor.operations.operators.core;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class MacroNode extends Node {
	
	String arg;
	
	public MacroNode(String macro) {
		arg = macro;
	}
	
	public boolean performNode () {
		Main.logDebug("Performing macro node"); // ----
		return Main.macroLauncher.launchMacro(arg, Operator.currentBlock.getLocation());
	}
	
	public static int getArgCount () {
		return 1;
	}
}
