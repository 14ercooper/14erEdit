package fourteener.worldeditor.operations.operators.variable;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.BlockVar;

public class SetBlockVarNode extends Node {

	public String name;
	
	public SetBlockVarNode newNode() {
		SetBlockVarNode node = new SetBlockVarNode();
		node.name = Main.operationParser.parseStringNode();
		return node;
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
	
	public int getArgCount () {
		return 1;
	}
}
