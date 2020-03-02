package fourteener.worldeditor.operations.operators.world;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.BlockVar;

public class SetBlockVarNode extends Node {

	public String name;
	
	public SetBlockVarNode(String val) {
		name = val;
	}
	
	public boolean performNode () {
		BlockVar bv = Operator.blockVars.get(name);
		Operator.currentBlock.setType(Material.matchMaterial(bv.getType()));
		if (!bv.getData().isEmpty()) {
			Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(bv.getData()));
		}
		if (!bv.getNBT().isEmpty()) {
			String command = "data merge block ";
			command += Operator.currentBlock.getLocation().getBlockX() + " ";
			command += Operator.currentBlock.getLocation().getBlockY() + " ";
			command += Operator.currentBlock.getLocation().getBlockZ() + " ";
			command += bv.getNBT();
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
		}
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
