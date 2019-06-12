package fourteener.worldeditor.operations.operators;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import fourteener.worldeditor.operations.Operator;

public class SetPlusNode extends Node {

	public Material arg1;
	public BlockData arg2;
	
	public static SetPlusNode newNode (String matData) {
		SetPlusNode setNode = new SetPlusNode();
		setNode.arg1 = Material.matchMaterial(matData.split("\\[")[0]);
		setNode.arg2 = Bukkit.getServer().createBlockData(matData);
		return setNode;
	}
	
	public boolean performNode () {
		Operator.currentBlock.setType(arg1);
		Operator.currentBlock.setBlockData(arg2);
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
