package com.fourteener.worldeditor.async;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.undo.Undo;
import com.fourteener.worldeditor.undo.UndoManager;

public class AsyncManager {
	
	// How heavy of a load should the async place on the server?
	public static final long delayTicks = 4;
	public static final long blockOperations = 10000;
	
	// Store operations
	private Queue<AsyncOperation> operations = new ArrayDeque<AsyncOperation>();
	
	// Store large operations
	private Queue<AsyncOperation> largeOps = new ArrayDeque<AsyncOperation>();
	
	// On initialization start the scheduler
	public AsyncManager () {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(GlobalVars.plugin, new Runnable () {
			@Override
			public void run () {
				GlobalVars.asyncManager.performOperation();
			}
		} , delayTicks, delayTicks);
	}
	
	// Schedule a new task cleanly
	public void scheduleEdit (Operator o, Player p, ArrayDeque<Block> b) {
		if (p == null) {
			operations.add(new AsyncOperation(o, b));
		}
		if (b.size() > Undo.maxBlocks) {
			largeOps.add(new AsyncOperation(o, p, b));
			p.sendMessage("§aThis is a large edit that cannot be undone.");
			p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");
			
		}
		else {
			operations.add(new AsyncOperation(true, p));
			operations.add(new AsyncOperation(o, p, b));
			operations.add(new AsyncOperation(false, p));
		}
	}
	
	public void scheduleEdit(Operator o, Player p, List<Block> b) {
		ArrayDeque<Block> d = new ArrayDeque<Block>();
		d.addAll(b);
		scheduleEdit(o, p, d);
	}
	
	// Confirm large edits
	public void confirmEdits (int number) {
		number = number < 1 ? 1 : number > largeOps.size() ? largeOps.size() : number;
		if (number < largeOps.size()) return;
		while (number-- > 0) {
			operations.add(largeOps.remove());
		}
	}
	
	// Cancel large edits
	public void cancelEdits (int number) {
		number = number < 1 ? 1 : number > largeOps.size() ? largeOps.size() : number;
		if (number < largeOps.size()) return;
		while (number-- > 0) {
			largeOps.remove();
		}
	}
	
	// Scheduled task to operate
	public void performOperation () {
		// If there isn't anything to do, return
		if (operations.size() == 0)
			return;
		
		// Limit operations per run
		long doneOperations = 0;
		
		// Loop until finished
		while (doneOperations < blockOperations && operations.size() > 0) {
			AsyncOperation op = operations.peek();
			if (op.key.equalsIgnoreCase("startundo")) {
				Main.logDebug("Async undo started for player " + op.player.getName());
				GlobalVars.currentUndo = UndoManager.getUndo(op.player);
				GlobalVars.currentUndo.startUndo();
				operations.remove();
				doneOperations += 100;
			}
			else if (op.key.equalsIgnoreCase("finishundo")) {
				GlobalVars.currentUndo.finishUndo();
				GlobalVars.currentUndo = null;
				Main.logDebug("Async undo finished for player " + op.player.getName());
				operations.remove();
				doneOperations += 100;
			}
			else if (op.key.equalsIgnoreCase("messyedit")) {
				Main.logDebug("Async messy edit started for player " + op.player.getName());
				operations.remove().operation.messyOperate();
				doneOperations += 10;
			}
			else if (op.key.equalsIgnoreCase("edit")) {
				Main.logDebug("Started async edit for player " + op.player.getName());
				while (doneOperations < blockOperations && op.toOperate.size() > 0) {
					Block b = op.toOperate.removeFirst();
					op.operation.operateOnBlock(b, op.player);
					doneOperations++;
				}
				if (op.toOperate.size() == 0) {
					operations.remove();
				}
			}
			else if (op.key.equalsIgnoreCase("rawedit")) {
				Main.logDebug("Started async raw edit for player " + op.player.getName());
				while (doneOperations < blockOperations && op.toOperate.size() > 0) {
					Block b = op.toOperate.removeFirst();
					op.operation.operateOnBlock(b);
					doneOperations++;
				}
				if (op.toOperate.size() == 0) {
					operations.remove();
				}
			}
			else {
				operations.remove();
			}
		}
		
		// Debug message
		if (doneOperations > 0) {
			Main.logDebug("Performed " + doneOperations + " async operations");
		}
	}
}
