package fourteener.worldeditor.operations.operators;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import fourteener.worldeditor.operations.Operator;

public class SetPlusNode extends Node {

	public BlockNode arg1;
	public BlockData arg2;
	
	public static SetPlusNode newNode (Node blockNode, String matData) {
		SetPlusNode setNode = new SetPlusNode();
		setNode.arg1 = (BlockNode) blockNode;
		setNode.arg2 = Bukkit.getServer().createBlockData(matData);
		return setNode;
	}
	
	public boolean performNode () {
		Operator.currentBlock.setType(arg1.getBlock(), Operator.ignoringPhysics);
		Operator.currentBlock.setBlockData(arg2, Operator.ignoringPhysics);
		return true;
	}
	
	public static int getArgCount () {
		return 2;
	}
}
