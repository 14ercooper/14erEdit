package fourteener.worldeditor.operations.operators.world;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class SetPlusNode extends Node {

	public Material arg1;
	public BlockData arg2;
	
	public SetPlusNode newNode() {
		SetPlusNode node = new SetPlusNode();
		String data = Main.operationParser.parseStringNode();
		node.arg1 = Material.matchMaterial(data.split("\\[")[0]);
		node.arg2 = Bukkit.getServer().createBlockData(data);
		return node;
	}
	
	public boolean performNode () {
		Operator.currentBlock.setType(arg1);
		Operator.currentBlock.setBlockData(arg2);
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
