package fourteener.worldeditor.operations.operators.world;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class SetPlusNode extends Node {

	public Material arg1;
	public BlockData arg2;
	
	public SetPlusNode(String matData) {
		arg1 = Material.matchMaterial(matData.split("\\[")[0]);
		arg2 = Bukkit.getServer().createBlockData(matData);
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
