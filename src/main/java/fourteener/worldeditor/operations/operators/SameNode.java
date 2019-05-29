package fourteener.worldeditor.operations.operators;

import org.bukkit.Material;

import fourteener.worldeditor.operations.Operator;

public class SameNode extends BlockNode {
	
	public static SameNode newNode () {
		return new SameNode();
	}
	
	public boolean performNode () {
		return true;
	}
	
	public Material getBlock () {
		return Operator.currentBlock.getType();
	}
	
	public static int getArgCount () {
		return 0;
	}

}
