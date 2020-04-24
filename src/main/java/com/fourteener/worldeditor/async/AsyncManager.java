package com.fourteener.worldeditor.async;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.main.NBTExtractor;
import com.fourteener.worldeditor.main.SetBlock;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.selection.Clipboard;
import com.fourteener.worldeditor.undo.UndoManager;

public class AsyncManager {

	// NBT extractor for clipboard
	NBTExtractor nbtExtractor = new NBTExtractor();

	// Store operations
	private ArrayList<AsyncOperation> operations = new ArrayList<AsyncOperation>();

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
		} , GlobalVars.ticksPerAsync, GlobalVars.ticksPerAsync);
	}

	// Drops the async queue
	public void dropAsync () {
		for (AsyncOperation asyncOp : operations) {
			if (asyncOp.undo != null && asyncOp.undoRunning)
				asyncOp.undo.finishUndo();
		}
		operations = new ArrayList<AsyncOperation>();
		largeOps = new ArrayDeque<AsyncOperation>();
	}

	// About how big is the async queue?
	public void asyncProgress (Player p) {
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
		int remTime = (int) (((double) remBlocks) / (GlobalVars.blocksPerAsync * (20.0 / GlobalVars.ticksPerAsync)));
		p.sendMessage("§aThere are " + remBlocks + " blocks in the async queue, for an estimated remaining time of less than " + remTime + " seconds.");
	}

	// Schedule a block iterator task
	public void scheduleEdit (Operator o, Player p, BlockIterator b) {
		if (p == null) {
			operations.add(new AsyncOperation(o, b));
		}
		else if (b.getTotalBlocks() > GlobalVars.undoLimit) {
			largeOps.add(new AsyncOperation(o, p, b));
			p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.");
			p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");

		}
		else {
			AsyncOperation asyncOp = new AsyncOperation(o, p, b);
			asyncOp.undo = UndoManager.getUndo(p);
			operations.add(asyncOp);
		}
	}

	public void scheduleEdit (Operator o, BlockIterator b) {
		operations.add(new AsyncOperation(o, b));
	}

	public void scheduleEdit (BlockIterator b, Player p, String path) {
		largeOps.add(new AsyncOperation(b, p, path));
		p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.");
		p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");
	}

	public void scheduleEdit (Player p, String path) {
		largeOps.add(new AsyncOperation(p, path));
		p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.");
		p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");
	}

	public void scheduleEdit (Clipboard c, ArrayDeque<Block> b, boolean save) {
		if (b.size() > GlobalVars.undoLimit) {
			largeOps.add(new AsyncOperation(c, b, save));
			c.owner.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.");
			c.owner.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");

		}
		else {
			AsyncOperation asyncOp = new AsyncOperation(c, b, save);
			asyncOp.undo = UndoManager.getUndo(c.owner);
			operations.add(asyncOp);
		}
	}

	public void scheduleEdit (Clipboard c, List<Block> b, boolean save) {
		ArrayDeque<Block> d = new ArrayDeque<Block>();
		d.addAll(b);
		scheduleEdit(c, d, save);
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
		while (doneOperations < GlobalVars.blocksPerAsync && operations.size() > 0) {
			int opSize = operations.size();
			for (int i = 0; i < opSize; i++) {
				AsyncOperation op = operations.get(i);
				if (op.key.equalsIgnoreCase("iteredit")) {
					Block b = null;
					b = op.blocks.getNext();
					if (op.undo != null) {
						if (!op.undoRunning) op.undo.startUndo();
						GlobalVars.currentUndo = op.undo;
						op.undoRunning = true;
					}
					if (b == null) {
						if (op.undo != null) {
							op.undo.finishUndo();
						}
						operations.remove(i);
						i--;
						opSize--;
						continue;
					}
					op.operation.operateOnBlock(b, op.player);
					doneOperations++;
					GlobalVars.currentUndo = null;
				}
				else if (op.key.equalsIgnoreCase("rawiteredit")) {
					Block b = null;
					b = op.blocks.getNext();
					if (op.undo != null) {
						if (!op.undoRunning) op.undo.startUndo();
						GlobalVars.currentUndo = op.undo;
						op.undoRunning = true;
					}
					if (b == null) {
						if (op.undo != null) {
							op.undo.finishUndo();
						}
						operations.remove(i);
						i--;
						opSize--;
						continue;
					}
					op.operation.operateOnBlock(b);
					doneOperations++;
					GlobalVars.currentUndo = null;
				}
				else if (op.key.equalsIgnoreCase("messyedit")) {
					operations.remove(i).operation.messyOperate();
					i--;
					opSize--;
					doneOperations += 100;
				}
				// TODO remove
				else if (op.key.equalsIgnoreCase("edit")) {
					Block b = op.toOperate.removeFirst();
					if (op.undo != null) {
						if (!op.undoRunning) op.undo.startUndo();
						GlobalVars.currentUndo = op.undo;
						op.undoRunning = true;
					}
					op.operation.operateOnBlock(b, op.player);
					doneOperations++;
					GlobalVars.currentUndo = null;
					if (op.toOperate.size() == 0) {
						if (op.undo != null) {
							op.undo.finishUndo();
						}
						operations.remove(i);
						i--;
						opSize--;
					}
				}
				// TODO remove
				else if (op.key.equalsIgnoreCase("rawedit")) {
					Block b = op.toOperate.removeFirst();
					if (op.undo != null) {
						if (!op.undoRunning) op.undo.startUndo();
						GlobalVars.currentUndo = op.undo;
						op.undoRunning = true;
					}
					op.operation.operateOnBlock(b);
					doneOperations++;
					GlobalVars.currentUndo = null;
					if (op.toOperate.size() == 0) {
						if (op.undo != null) {
							op.undo.finishUndo();
						}
						operations.remove(i);
						i--;
						opSize--;
					}
				}
				// TODO update to new system
				else if (op.key.equalsIgnoreCase("saveclip")) {
					Block b = op.toOperate.removeFirst();
					int xB = Math.abs(b.getX() - op.clipboard.xNeg);
					int yB = Math.abs(b.getY() - op.clipboard.yNeg);
					int zB = Math.abs(b.getZ() - op.clipboard.zNeg);
					op.clipboard.blockData.set(xB + (zB * op.clipboard.width) + (yB * op.clipboard.length * op.clipboard.width), b.getBlockData().getAsString());
					op.clipboard.nbtData.set(xB + (zB * op.clipboard.width) + (yB * op.clipboard.length * op.clipboard.width), nbtExtractor.getNBT(b.getState()));
					doneOperations++;
					if (op.toOperate.size() == 0) {
						op.clipboard.owner.sendMessage("§dSelection copied");
						operations.remove(i);
						i--;
						opSize--;
					}
				}
				// TODO update to new system
				else if (op.key.equalsIgnoreCase("loadclip")) {
					Block b = op.toOperate.removeFirst();
					if (op.undo != null) {
						if (!op.undoRunning) op.undo.startUndo();
						GlobalVars.currentUndo = op.undo;
						op.undoRunning = true;
					}
					Material blockMat = Material.matchMaterial(op.clipboard.blockData.get(op.clipboard.loadPos).split("\\[")[0]);
					BlockData blockDat = Bukkit.getServer().createBlockData(op.clipboard.blockData.get(op.clipboard.loadPos));
					String nbt = op.clipboard.nbtData.get(op.clipboard.loadPos);
					// Set the block
					if (blockMat == Material.AIR) {
						if (op.clipboard.setAir) {
							SetBlock.setMaterial(b, blockMat);
							b.setBlockData(blockDat);
							if (!nbt.equalsIgnoreCase("")) {
								String command = "data merge block " + b.getX() + " " + b.getY() + " " + b.getZ() + " " + nbt;
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
							}
						}
					}
					else {
						SetBlock.setMaterial(b, blockMat);
						b.setBlockData(blockDat);
						if (!nbt.equalsIgnoreCase("")) {
							String command = "data merge block " + b.getX() + " " + b.getY() + " " + b.getZ() + " " + nbt;
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
						}
					}
					op.clipboard.loadPos++;
					doneOperations++;
					GlobalVars.currentUndo = null;
					if (op.toOperate.size() == 0) {
						if (op.undo != null) {
							op.undo.finishUndo();
						}
						op.clipboard.owner.sendMessage("§dSelection pasted");
						operations.remove(i);
						i--;
						opSize--;
					}
				}
				// TODO: saveschem
				// TODO: loadschem
				else {
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
