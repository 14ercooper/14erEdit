package fourteener.worldeditor.operations.operators;

import org.bukkit.block.Block;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;

public class BlocksAboveNode extends Node {
	
	RangeNode arg1;
	Node arg2;
	
	public static BlocksAboveNode newNode (RangeNode range, Node block) {
		BlocksAboveNode baNode = new BlocksAboveNode();
		baNode.arg1 = range;
		baNode.arg2 = block;
		return baNode;
	}
	
	public boolean performNode () {
		Block currBlock = Operator.currentBlock;
		int x = currBlock.getX();
		int y = currBlock.getY();
		int z = currBlock.getZ();
		int min = (int) arg1.getMin();
		int max = (int) arg1.getMax();
		boolean blockRangeMet = true;
		
		for (int dy = y + min; dy <= y + max; dy++) {
			Operator.currentBlock = Main.world.getBlockAt(x, dy, z);
			if (!(arg2.performNode()))
				blockRangeMet = false;
		}
		
		Operator.currentBlock = currBlock;
		return blockRangeMet;
	}
	
	public static int getArgCount () {
		return 2;
	}
}
