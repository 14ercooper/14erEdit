package com.fourteener.worldeditor.async;

import java.util.ArrayDeque;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitScheduler;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.undo.UndoManager;

public class AsyncManager {
	
	// How heavy of a load should the async place on the server?
	long delayTicks = 4;
	long blockOperations = 10000;
	
	// Store operations
	private Queue<AsyncOperation> operations = new ArrayDeque<AsyncOperation>();
	
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
				UndoManager.getUndo(op.player).startUndo();
				operations.remove();
				doneOperations += 100;
			}
			else if (op.key.equalsIgnoreCase("finishundo")) {
				UndoManager.getUndo(op.player).finishUndo();
				Main.logDebug("Async undo finished for player " + op.player.getName());
				operations.remove();
				doneOperations += 100;
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
