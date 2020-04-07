package com.fourteener.worldeditor.undo;

import java.util.ArrayDeque;
import java.util.HashSet;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public class Undo {
	// Whose undo is this?
	public Player owner;
	
	// How many blocks should be stored
	private int maxBlocks = 1000000;
	
	// Stores undo and redo elements
	private ArrayDeque<BlockState> undoList = new ArrayDeque<BlockState>();
	private ArrayDeque<BlockState> redoList = new ArrayDeque<BlockState>();
	private ArrayDeque<Integer> undoSizes = new ArrayDeque<Integer>();
	private ArrayDeque<Integer> redoSizes = new ArrayDeque<Integer>();
	
	// For consolidating undos
	private HashSet<String> positions = new HashSet<String>();
	
	Undo(Player player) {
		owner = player;
	}
	
	// Start an undo
	public void startUndo () {
		positions = new HashSet<String>();
	}
	
	// End an undo
	public int finishUndo () {
		if (positions.size() <= 0) {
			return 0;
		}
		undoSizes.addFirst(positions.size());
		return positions.size();
	}
	
	public boolean storeBlock(BlockState bs) {
		int x = bs.getX();
		int y = bs.getY();
		int z = bs.getZ();
		String posStr = x + ";" + y + ";" + z;
		if (!positions.contains(posStr)) {
			positions.add(posStr);
			undoList.add(bs);
			if (undoList.size() > maxBlocks) {
				int numRem = undoSizes.removeLast();
				while (numRem-- > 0) {
					undoList.removeLast();
				}
			}
			return true;
		}
		return false;
	}
	
	public void storeBlock(Block b) {
		storeBlock(b.getState());
	}
	
	// Undo a number of changes
	public int undoChanges (int number) {
		int numPlaced = 0;
		while (number-- > 0) {
			int numRem = undoSizes.removeFirst();
			redoSizes.addFirst(numRem);
			numPlaced += numRem;
			while (numRem-- > 0) {
				BlockState bs = undoList.removeFirst();
				redoList.addFirst(bs);
				Block b = bs.getBlock();
				b.setType(bs.getType(), true);
				b.setBlockData(bs.getBlockData(), true);
			}
			if (redoList.size() > maxBlocks) {
				numRem = redoSizes.removeLast();
				while (numRem-- > 0) {
					redoList.removeLast();
				}
			}
		}
		return numPlaced;
	}
	
	// Redo a number of changes
	public int redoChanges (int number) {
		int numPlaced = 0;
		while (number-- > 0) {
			int numRem = redoSizes.removeFirst();
			undoSizes.addFirst(numRem);
			numPlaced += numRem;
			while (numRem-- > 0) {
				BlockState bs = redoList.removeFirst();
				undoList.addFirst(bs);
				Block b = bs.getBlock();
				b.setType(bs.getType(), true);
				b.setBlockData(bs.getBlockData(), true);
			}
			if (undoList.size() > maxBlocks) {
				numRem = undoSizes.removeLast();
				while (numRem-- > 0) {
					undoList.removeLast();
				}
			}
		}
		return numPlaced;
	}
}
