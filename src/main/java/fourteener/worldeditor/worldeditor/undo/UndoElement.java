package fourteener.worldeditor.worldeditor.undo;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import fourteener.worldeditor.main.Main;

public class UndoElement {
	private List<BlockState> blocks = new ArrayList<BlockState>();
	
	// Create a new undo element
	public static UndoElement newUndoElement (List<Block> blockList) {
		UndoElement e = new UndoElement();
		List<BlockState> list = new ArrayList<BlockState>();
		for (Block b : blockList) {
			list.add(b.getState());
		}
		e.blocks = list;
		return e;
	}
	
	// Create a new undo element using block states
	public static UndoElement newUndoElementFromStates (List<BlockState> blockList) {
		UndoElement e = new UndoElement();
		e.blocks = blockList;
		return e;
	}
	
	// Revert the slice of the world where this undo element is from back to how it was when this element was registered
	public boolean applyElement () {
		for (BlockState b : blocks) {
			Block block = Main.world.getBlockAt(b.getLocation());
			block.setType(b.getType());
			block.setBlockData(b.getBlockData());
		}
		return true;
	}
	
	// Return an element which is overlayed over this one, but with the current state of the world
	public UndoElement getInverseElement () {
		List<Block> blockList = new ArrayList<Block>();
		for (BlockState b : blocks) {
			blockList.add(Main.world.getBlockAt(b.getLocation()));
		}
		return newUndoElement(blockList);
	}
	
	public List<BlockState> getBlocks () {
		return blocks;
	}
}
