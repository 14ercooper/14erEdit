package com._14ercooper.worldeditor.undo;

import java.util.ArrayDeque;
import java.util.HashSet;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class Undo {
	// Whose undo is this?
	public Player owner;

	// Stores undo and redo elements
	private ArrayDeque<BlockState> undoList = new ArrayDeque<BlockState>();
	private ArrayDeque<BlockState> redoList = new ArrayDeque<BlockState>();
	private ArrayDeque<Integer> undoSizes = new ArrayDeque<Integer>();
	private ArrayDeque<Integer> redoSizes = new ArrayDeque<Integer>();

	private int nestedUndos = 0;

	// For consolidating undos
	private HashSet<String> positions = new HashSet<String>();

	Undo(Player player) {
		owner = player;
	}

	// Start an undo
	public void startUndo () {
		Main.logDebug("Starting undo for " + owner.getName());
		if (nestedUndos == 0) {
			positions = new HashSet<String>();
		}
		nestedUndos++;
	}

	// End an undo
	public int finishUndo () {
		Main.logDebug("Finished undo for " + owner.getName());
		nestedUndos--;
		if (nestedUndos == 0) {
			Main.logDebug(positions.size() + " block changes stored");
			if (positions.size() <= 0) {
				return 0;
			}
			undoSizes.addFirst(positions.size());
			return positions.size();
		}
		return 0;
	}

	public boolean storeBlock(BlockState bs) {
		int x = bs.getX();
		int y = bs.getY();
		int z = bs.getZ();
		String posStr = x + ";" + y + ";" + z;
		if (!positions.contains(posStr)) {
			positions.add(posStr);
			undoList.addLast(bs);
			while (undoList.size() > GlobalVars.undoLimit) {
				int numRem = undoSizes.removeLast();
				while (numRem-- > 0) {
					undoList.removeFirst();
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
		try {
			number = number < 1 ? 1 : number > undoSizes.size() ? undoSizes.size() : number;
			int numPlaced = 0;
			Main.logDebug("Undoing " + number + " edits");
			while (number-- > 0) {
				int numRem = undoSizes.removeFirst();
				numPlaced += numRem;
				Main.logDebug("Undoing " + numRem + " block edits");
				while (numRem-- > 0) {
					BlockState bs = undoList.removeLast();
					Block b = bs.getBlock();
					redoList.addFirst(b.getState());
					b.setType(bs.getType(), false);
					b.setBlockData(bs.getBlockData(), false);
				}
			}
			redoSizes.addFirst(numPlaced);
			while (redoList.size() > GlobalVars.undoLimit) {
				int numRem = redoSizes.removeLast();
				while (numRem-- > 0) {
					redoList.removeLast();
				}
			}
			return numPlaced;
		} catch (Exception e) {
			Main.logError("Could not perform an undo. Is there anything to undo?", Operator.currentPlayer);
			return 0;
		}
	}

	// Redo a number of changes
	public int redoChanges (int number) {
		try {
			number = number < 1 ? 1 : number > redoSizes.size() ? redoSizes.size() : number;
			int numPlaced = 0;
			Main.logDebug("Redoing " + number + " edits");
			while (number-- > 0) {
				int numRem = redoSizes.removeFirst();
				numPlaced += numRem;
				Main.logDebug("Redoing " + numRem + " block edits");
				while (numRem-- > 0) {
					BlockState bs = redoList.removeFirst();
					undoList.addLast(bs);
					Block b = bs.getBlock();
					b.setType(bs.getType(), false);
					b.setBlockData(bs.getBlockData(), false);
				}
			}
			undoSizes.addFirst(numPlaced);
			while (undoList.size() > GlobalVars.undoLimit) {
				int numRem = undoSizes.removeLast();
				while (numRem-- > 0) {
					undoList.removeLast();
				}
			}
			return numPlaced;
		} catch (Exception e) {
			Main.logError("Could not perform redo. Is there anything to redo?", Operator.currentPlayer);
			return 0;
		}
	}
}
