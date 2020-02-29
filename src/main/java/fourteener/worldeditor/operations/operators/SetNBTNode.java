package fourteener.worldeditor.operations.operators;

import org.bukkit.Bukkit;

import fourteener.worldeditor.operations.Operator;

public class SetNBTNode extends Node {
	
	String nbt;
	
	public static SetNBTNode newNode (String val) {
		SetNBTNode node = new SetNBTNode();
		node.nbt = val;
		return node;
	}
	
	public boolean performNode () {
		String command = "data merge block ";
		command += Operator.currentBlock.getLocation().getBlockX() + " ";
		command += Operator.currentBlock.getLocation().getBlockY() + " ";
		command += Operator.currentBlock.getLocation().getBlockZ() + " ";
		command += nbt;
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
