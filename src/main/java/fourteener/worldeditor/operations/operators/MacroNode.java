package fourteener.worldeditor.operations.operators;

import org.bukkit.Bukkit;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.macros.MacroLauncher;

public class MacroNode extends Node {
	
	String arg;
	
	public static MacroNode newNode (String macro) {
		MacroNode macroNode = new MacroNode();
		macroNode.arg = macro;
		return macroNode;
	}
	
	public boolean performNode () {
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Performing macro node"); // ----
		return MacroLauncher.launchMacro(arg, Operator.currentBlock.getLocation());
	}
	
	public static int getArgCount () {
		return 1;
	}
}
