package com._14ercooper.worldeditor.async;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com._14ercooper.schematics.SchemLite;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.SchemBrushIterator;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.undo.UndoManager;

public class AsyncManager {

    // NBT extractor for clipboard
    NBTExtractor nbtExtractor = new NBTExtractor();

    // Flag queue dropped
    boolean queueDropped = false;

    // Store operations
    private ArrayList<AsyncOperation> operations = new ArrayList<AsyncOperation>();

    // Store large operations
    private Queue<AsyncOperation> largeOps = new ArrayDeque<AsyncOperation>();

    // On initialization start the scheduler
    public AsyncManager() {
	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	scheduler.scheduleSyncRepeatingTask(GlobalVars.plugin, new Runnable() {
	    @Override
	    public void run() {
		GlobalVars.asyncManager.performOperation();
	    }
	}, GlobalVars.ticksPerAsync, GlobalVars.ticksPerAsync);
    }

    // Drops the async queue
    public void dropAsync() {
	Main.logDebug("Async queue dropped.");
	for (AsyncOperation asyncOp : operations) {
	    if (asyncOp.undo != null && asyncOp.undoRunning)
		asyncOp.undo.finishUndo();
	}
	operations = new ArrayList<AsyncOperation>();
	largeOps = new ArrayDeque<AsyncOperation>();
	queueDropped = true;
    }

    // How many blocks do we have less
    public long getRemainingBlocks() {
	long remBlocks = 0;
	for (AsyncOperation a : operations) {
	    if (a.blocks != null) {
		remBlocks += a.blocks.getRemainingBlocks();
	    }
	    else if (a.toOperate != null) {
		remBlocks += a.toOperate.size();
	    }
	    else {
		remBlocks += 100;
	    }
	}
	return remBlocks;
    }

    // About how big is the async queue?
    public void asyncProgress(CommandSender p) {
	long remBlocks = getRemainingBlocks();
	int remTime = (int) (((double) remBlocks) / (GlobalVars.blocksPerAsync * (20.0 / GlobalVars.ticksPerAsync)));
	p.sendMessage("§aThere are " + remBlocks
		+ " blocks in the async queue, for an estimated remaining time of less than " + remTime + " seconds.");
    }
    
    // Dump some data about what's in the async queue
    public void asyncDump(CommandSender p) {
	p.sendMessage("§aThere are currently " + operations.size() + " operations in the async queue for " + getRemainingBlocks() +
		" blocks.");
	p.sendMessage("§aThe large edits confirm queue has " + largeOps.size() + " pending operations.");
	p.sendMessage("§aPer Operation Stats:");
	for (AsyncOperation a : operations) {
		p.sendMessage("§a[Running] " + a.blocks.getRemainingBlocks() + " blocks remaining out of " + a.blocks.getTotalBlocks() + " total blocks. Undo? " + a.undoRunning + " " + a.blocks.toString());
	}
	for (AsyncOperation a : largeOps) {
		p.sendMessage("§a[Large Queue] " + a.blocks.getTotalBlocks() + " total blocks. " + a.blocks.toString());
	}
    }

    // Schedule a block iterator task
    public void scheduleEdit(Operator o, Player p, BlockIterator b) {
	if (p == null) {
	    operations.add(new AsyncOperation(o, b));
	}
	else if (b.getTotalBlocks() > GlobalVars.undoLimit) {
	    largeOps.add(new AsyncOperation(o, p, b));
	    if (!GlobalVars.autoConfirm) {
		p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.");
		p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");
	    }

	}
	else {
	    AsyncOperation asyncOp = new AsyncOperation(o, p, b);
	    asyncOp.undo = UndoManager.getUndo(p);
	    operations.add(asyncOp);
	}
    }

    // Schedule a nested block iterator task
    public void scheduleEdit(Operator o, Player p, BlockIterator b, boolean force) {
	if (p == null)
	    operations.add(new AsyncOperation(o, b));
	else if (b.getTotalBlocks() > GlobalVars.undoLimit) {
	    AsyncOperation asyncOp = new AsyncOperation(o, p, b);
	    operations.add(asyncOp);
	}
	else {
	    AsyncOperation asyncOp = new AsyncOperation(o, p, b);
	    asyncOp.undo = UndoManager.getUndo(p);
	    operations.add(asyncOp);
	}
    }

    public void scheduleEdit(Operator o, BlockIterator b) {
	operations.add(new AsyncOperation(o, b));
    }

    // Schedule a multibrush operation
    public void scheduleEdit(List<BlockIterator> iterators, List<Operator> operations, Player p) {
	AsyncOperation asyncOp = new AsyncOperation(iterators, operations, p);
	largeOps.add(asyncOp);
	if (!GlobalVars.autoConfirm) {
	    p.sendMessage("§aMultibrushes can only be run in large edit mode without undos.");
	    p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");
	}
    }

    // Schedule a schematics operation
    public void scheduleEdit(SchemLite sl, boolean saveSchem, Player p, int[] origin) {
	AsyncOperation asyncOp = new AsyncOperation(sl, saveSchem, origin, p);
	if (asyncOp.blocks.getTotalBlocks() > GlobalVars.undoLimit) {
	    largeOps.add(asyncOp);
	    if (!GlobalVars.autoConfirm) {
		p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.");
		p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");
	    }
	}
	else {
	    asyncOp.undo = UndoManager.getUndo(p);
	    operations.add(asyncOp);
	}
    }

    // Schedule a selection clone operation
    public void scheduleEdit(BlockIterator b, int[] offset, int times, boolean delOriginal, Player p) {
	AsyncOperation asyncOp = new AsyncOperation(b, offset, times, delOriginal, p);
	if (asyncOp.blocks.getTotalBlocks() * times > GlobalVars.undoLimit) {
	    largeOps.add(asyncOp);
	    if (!GlobalVars.autoConfirm) {
		p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.");
		p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");
	    }
	}
	else {
	    asyncOp.undo = UndoManager.getUndo(p);
	    operations.add(asyncOp);
	}
    }

    // Confirm large edits
    public void confirmEdits(int number) {
	number = number < 1 ? 1 : number > largeOps.size() ? largeOps.size() : number;
	Main.logDebug("Confirming " + number + " large edits.");
//	if (number < largeOps.size())
//	    return;
	while (number-- > 0) {
	    operations.add(largeOps.remove());
	}
    }

    // Cancel large edits
    public void cancelEdits(int number) {
	number = number < 1 ? 1 : number > largeOps.size() ? largeOps.size() : number;
	Main.logDebug("Cancelling " + number + " large edits.");
//	if (number < largeOps.size())
//	    return;
	while (number-- > 0) {
	    largeOps.remove();
	}
    }

    // Scheduled task to operate
    public void performOperation() {
	// If in autoconfirm mode, do that
	if (GlobalVars.autoConfirm) {
	    confirmEdits(10);
	}

	// If there isn't anything to do, return
	if (operations.size() == 0)
	    return;

	// Limit operations per run
	long doneOperations = 0;

	if (queueDropped) {
	    queueDropped = false;
	}

	GlobalVars.errorLogged = false;

	// Loop until finished
	while (doneOperations < GlobalVars.blocksPerAsync && operations.size() > 0) {
	    if (queueDropped) {
		queueDropped = false;
		return;
	    }
	    int opSize = operations.size();
	    for (int i = 0; i < opSize; i++) {
		if (queueDropped) {
		    queueDropped = false;
		    return;
		}
		AsyncOperation currentAsyncOp = operations.get(i);
		
		if (currentAsyncOp.blocks.getRemainingBlocks() == 0) {
			if (currentAsyncOp.undo != null) {
			    currentAsyncOp.undo.finishUndo();
			}
			if (currentAsyncOp.blocks instanceof SchemBrushIterator) {
			    ((SchemBrushIterator) currentAsyncOp.blocks).cleanup();
			}
			operations.remove(i);
			i--;
			opSize--;
			continue;
		}

		// Iterator edit
		if (currentAsyncOp.key.equalsIgnoreCase("iteredit")) {
		    Block b = null;
		    b = currentAsyncOp.blocks.getNext();
		    if (currentAsyncOp.undo != null) {
			if (!currentAsyncOp.undoRunning)
			    currentAsyncOp.undo.startUndo();
			GlobalVars.currentUndo = currentAsyncOp.undo;
			currentAsyncOp.undoRunning = true;
		    }
		    if (b == null) {
			if (currentAsyncOp.undo != null) {
			    currentAsyncOp.undo.finishUndo();
			}
			if (currentAsyncOp.blocks instanceof SchemBrushIterator) {
			    ((SchemBrushIterator) currentAsyncOp.blocks).cleanup();
			}
			operations.remove(i);
			i--;
			opSize--;
			continue;
		    }
		    currentAsyncOp.operation.operateOnBlock(b, currentAsyncOp.player);
		    doneOperations++;
		    GlobalVars.currentUndo = null;
		}

		// Raw iterator edit
		else if (currentAsyncOp.key.equalsIgnoreCase("rawiteredit")) {
		    Block b = null;
		    b = currentAsyncOp.blocks.getNext();
		    if (currentAsyncOp.undo != null) {
			if (!currentAsyncOp.undoRunning)
			    currentAsyncOp.undo.startUndo();
			GlobalVars.currentUndo = currentAsyncOp.undo;
			currentAsyncOp.undoRunning = true;
		    }
		    if (b == null) {
			if (currentAsyncOp.undo != null) {
			    currentAsyncOp.undo.finishUndo();
			}
			if (currentAsyncOp.blocks instanceof SchemBrushIterator) {
			    ((SchemBrushIterator) currentAsyncOp.blocks).cleanup();
			}
			operations.remove(i);
			i--;
			opSize--;
			continue;
		    }
		    currentAsyncOp.operation.operateOnBlock(b);
		    doneOperations++;
		    GlobalVars.currentUndo = null;
		}

		// Multibrush
		else if (currentAsyncOp.key.equalsIgnoreCase("multibrush")) {
		    Block b = null;
		    boolean doContinue = false;
		    while (true) {
			b = currentAsyncOp.iterators.get(0).getNext();
			if (b != null) {
			    break;
			}
			if (b == null && currentAsyncOp.iterators.size() > 1) {
			    currentAsyncOp.iterators.remove(0);
				if (currentAsyncOp.iterators.get(0) instanceof SchemBrushIterator) {
				    ((SchemBrushIterator) currentAsyncOp.iterators.get(0)).cleanup();
				}
			    currentAsyncOp.operations.remove(0);
			    if (currentAsyncOp.iterators.size() == 0 || currentAsyncOp.operations.size() == 0) {
				doContinue = true;
				break;
			    }
			}
			else {
			    doContinue = true;
			    break;
			}
		    }
		    if (doContinue) {
			this.operations.remove(i);
			i--;
			opSize--;
			continue;
		    }
		    currentAsyncOp.operations.get(0).operateOnBlock(b, currentAsyncOp.player);
		    doneOperations++;
		    GlobalVars.currentUndo = null;
		}

		else if (currentAsyncOp.key.equalsIgnoreCase("messyedit")) {
		    operations.remove(i).operation.messyOperate();
		    i--;
		    opSize--;
		    doneOperations += 100;
		}

		// Save schematic
		else if (currentAsyncOp.key.equalsIgnoreCase("saveschem")) {
		    Block b = null;
		    b = currentAsyncOp.blocks.getNext();
		    if (currentAsyncOp.undo != null) {
			if (!currentAsyncOp.undoRunning) {
			    currentAsyncOp.undo.startUndo();
			    try {
				currentAsyncOp.schem.resetWrite();
			    }
			    catch (IOException e) {
				Main.logError("Could not write to schematic file", currentAsyncOp.player);
			    }
			}
			GlobalVars.currentUndo = currentAsyncOp.undo;
			currentAsyncOp.undoRunning = true;
		    }
		    if (b == null) {
			if (currentAsyncOp.undo != null) {
			    currentAsyncOp.undo.finishUndo();
			}
			operations.remove(i);
			i--;
			opSize--;
			currentAsyncOp.player.sendMessage("§aSelection saved");
			continue;
		    }
		    String material = b.getType().toString();
		    String data = b.getBlockData().getAsString();
		    NBTExtractor nbtE = new NBTExtractor();
		    String nbt = nbtE.getNBT(b);
		    try {
			currentAsyncOp.schem.writeBlock(material, data, nbt);
		    }
		    catch (IOException e) {
			// Don't need to do anything
		    }
		    doneOperations++;
		    GlobalVars.currentUndo = null;
		}

		// Load schematic
		else if (currentAsyncOp.key.equalsIgnoreCase("loadschem")) {
		    Block b = null;
		    b = currentAsyncOp.blocks.getNext();
		    if (currentAsyncOp.undo != null) {
			if (!currentAsyncOp.undoRunning) {
			    currentAsyncOp.undo.startUndo();
			}
			GlobalVars.currentUndo = currentAsyncOp.undo;
			currentAsyncOp.undoRunning = true;
		    }
		    if (b == null) {
			if (currentAsyncOp.undo != null) {
			    currentAsyncOp.undo.finishUndo();
			}
			operations.remove(i);
			i--;
			opSize--;
			try {
			    currentAsyncOp.schem.closeRead();
			}
			catch (IOException e) {
			    // Don't need to do anything
			}
			currentAsyncOp.player.sendMessage("§aData loaded");
			continue;
		    }
		    String[] results = {};
		    try {
			results = currentAsyncOp.schem.readNext();
		    }
		    catch (IOException e) {
			// Don't need to do anything
		    }
		    if (!Material.matchMaterial(results[0]).isAir() || currentAsyncOp.schem.setAir()) {
			SetBlock.setMaterial(b, Material.matchMaterial(results[0]), false);
			b.setBlockData(Bukkit.getServer().createBlockData(results[1]));
			if (!results[2].isEmpty()) {
			    String command = "data merge block " + b.getX() + " " + b.getY() + " " + b.getZ() + " "
				    + results[2];
			    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
		    }
		    doneOperations++;
		    GlobalVars.currentUndo = null;
		}

		// Selection move/stack
		else if (currentAsyncOp.key.equalsIgnoreCase("selclone")) {
		    Player tempPlayer = Operator.currentPlayer;
		    Operator.currentPlayer = currentAsyncOp.player;
		    Block b = currentAsyncOp.blocks.getNext();
		    Operator.currentPlayer = tempPlayer;
		    if (currentAsyncOp.undo != null) {
			if (!currentAsyncOp.undoRunning) {
			    currentAsyncOp.undo.startUndo();
			}
			GlobalVars.currentUndo = currentAsyncOp.undo;
			currentAsyncOp.undoRunning = true;
		    }
		    if (b == null) {
			if (currentAsyncOp.undo != null) {
			    currentAsyncOp.undo.finishUndo();
			}
			operations.remove(i);
			i--;
			opSize--;
			continue;
		    }
		    // Actually do the clone
		    for (int timesDone = 0; timesDone < currentAsyncOp.times; timesDone++) {
			Block toEdit = b.getRelative(currentAsyncOp.offset[0] * (1 + timesDone),
				currentAsyncOp.offset[1] * (1 + timesDone), currentAsyncOp.offset[2] * (1 + timesDone));
			SetBlock.setMaterial(toEdit, b.getType(), false);
			toEdit.setBlockData(b.getBlockData(), false);
			NBTExtractor nbt = new NBTExtractor();
			String nbtStr = nbt.getNBT(b);
			if (nbtStr.length() > 2) {
			    String command = "data merge block " + toEdit.getX() + " " + toEdit.getY() + " "
				    + toEdit.getZ() + " " + nbtStr;
			    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
			    doneOperations++;
			}
		    }
		    if (currentAsyncOp.delOriginal) {
			SetBlock.setMaterial(b, Material.AIR, false);
			doneOperations++;
		    }
		    doneOperations += currentAsyncOp.times;
		    GlobalVars.currentUndo = null;
		}

		else {
		    Main.logError("Invalid operation in async queue. Removing operation.", Bukkit.getConsoleSender());
		    operations.remove(i);
		    i--;
		    opSize--;
		}
	    }
	}

	// Debug message
	if (doneOperations > 0) {
	    Main.logDebug("Performed " + doneOperations + " async operations");
	}
    }
}
