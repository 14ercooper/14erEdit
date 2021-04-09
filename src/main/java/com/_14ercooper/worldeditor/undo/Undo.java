package com._14ercooper.worldeditor.undo;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.HashSet;

public class Undo {
    // Whose undo is this?
    public final Player owner;

    // Stores undo and redo elements
    private final ArrayDeque<BlockState> undoList = new ArrayDeque<>();
    private final ArrayDeque<BlockState> redoList = new ArrayDeque<>();
    private final ArrayDeque<Integer> undoSizes = new ArrayDeque<>();
    private final ArrayDeque<Integer> redoSizes = new ArrayDeque<>();

    private int nestedUndos = 0;

    private long runningUndoSize = 0;

    // For consolidating undos
    private HashSet<String> positions = new HashSet<>();

    Undo(Player player) {
        owner = player;
    }
    
    // Can we start an undo with this size?
    public boolean canStartUndo(long size) {
//	System.out.println(runningUndoSize + size <= GlobalVars.undoLimit);
	return runningUndoSize + size <= GlobalVars.undoLimit;
    }
    
    public void startTrackingUndo(long size) {
	runningUndoSize += size;
    }

    // Start an undo
    public void startUndo(long size) {
	Main.logDebug("Starting undo for " + owner.getName());
	if (nestedUndos == 0) {
        positions = new HashSet<>();
    }
	nestedUndos++;

    }

    // End an undo
    public void finishUndo() {
        Main.logDebug("Finished undo for " + owner.getName());
        nestedUndos--;
        if (nestedUndos == 0) {
            Main.logDebug(positions.size() + " block changes stored");
            runningUndoSize = 0;
            if (positions.size() <= 0) {
                return;
            }
            undoSizes.addFirst(positions.size());
        }
    }

    public void storeBlock(BlockState bs) {
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
        }
    }

    public void storeBlock(Block b) {
	storeBlock(b.getState());
    }

    // Undo a number of changes
    public int undoChanges(int number) {
	try {
        number = number < 1 ? 1 : Math.min(number, undoSizes.size());
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
	}
	catch (Exception e) {
	    Main.logError("Could not perform an undo. Is there anything to undo?", Operator.currentPlayer, e);
	    return 0;
	}
    }

    // Redo a number of changes
    public int redoChanges(int number) {
	try {
        number = number < 1 ? 1 : Math.min(number, redoSizes.size());
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
	}
	catch (Exception e) {
	    Main.logError("Could not perform redo. Is there anything to redo?", Operator.currentPlayer, e);
	    return 0;
	}
    }
}
