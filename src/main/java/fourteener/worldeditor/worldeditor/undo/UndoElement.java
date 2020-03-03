package fourteener.worldeditor.worldeditor.undo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import fourteener.worldeditor.main.Main;

public class UndoElement {
	private Set<BlockState> blocks = new HashSet<BlockState>();
	
	// Create a new undo element
	public UndoElement(List<Block> blockList) {
		for (Block b : blockList) {
			blocks.add(b.getState());
		}
	}
	
	// Create a new undo element using block states
	public UndoElement(Set<BlockState> blockList) {
		blocks = blockList;
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
		return new UndoElement(blockList);
	}
	
	public Set<BlockState> getBlocks () {
		return blocks;
	}
}
