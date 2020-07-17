package com._14ercooper.worldeditor.async;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com._14ercooper.schematics.SchemLite;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.undo.UndoManager;

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
	public void asyncProgress (CommandSender p) {
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
	
	// Schedule a schematics operation
	public void scheduleEdit (SchemLite sl, boolean saveSchem, Player p, int[] origin) {
		AsyncOperation asyncOp = new AsyncOperation(sl, saveSchem, origin, p);
		if (asyncOp.blocks.getTotalBlocks() > GlobalVars.undoLimit) {
			largeOps.add(asyncOp);
			p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.");
			p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");
		}
		else {
			asyncOp.undo = UndoManager.getUndo(p);
			operations.add(asyncOp);
		}
	}
	
	// Schedule a selection clone operation
	public void scheduleEdit (BlockIterator b, int[] offset, int times, boolean delOriginal, Player p) {
		AsyncOperation asyncOp = new AsyncOperation(b, offset, times, delOriginal, p);
		if (asyncOp.blocks.getTotalBlocks() * times > GlobalVars.undoLimit) {
			largeOps.add(asyncOp);
			p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.");
			p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel");
		}
		else {
			asyncOp.undo = UndoManager.getUndo(p);
			operations.add(asyncOp);
		}
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
			GlobalVars.errorLogged = false;
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
				
				// Save schematic
				else if (op.key.equalsIgnoreCase("saveschem")) {
					Block b = null;
					b = op.blocks.getNext();
					if (op.undo != null) {
						if (!op.undoRunning) {
							op.undo.startUndo();
							try {
								op.schem.resetWrite();
							} catch (IOException e) {
								Main.logError("Could not write to schematic file", op.player);
							}
						}
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
						op.player.sendMessage("§aSelection saved");
						continue;
					}
					String material = b.getType().toString();
					String data = b.getBlockData().getAsString();
					NBTExtractor nbtE = new NBTExtractor();
					String nbt = nbtE.getNBT(b);
					try {
						op.schem.writeBlock(material, data, nbt);
					} catch (IOException e) {
						// Don't need to do anything
					}
					doneOperations++;
					GlobalVars.currentUndo = null;
				}
				
				// Load schematic
				else if (op.key.equalsIgnoreCase("loadschem")) {
					Block b = null;
					b = op.blocks.getNext();
					if (op.undo != null) {
						if (!op.undoRunning) {
							op.undo.startUndo();
						}
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
						try {
							op.schem.closeRead();
						} catch (IOException e) {
							// Don't need to do anything
						}
						op.player.sendMessage("§aData loaded");
						continue;
					}
					String[] results = {};
					try {
						results = op.schem.readNext();
					} catch (IOException e) {
						// Don't need to do anything
					}
					if (!Material.matchMaterial(results[0]).isAir() || op.schem.setAir()) {
						SetBlock.setMaterial(b, Material.matchMaterial(results[0]), false);
						b.setBlockData(Bukkit.getServer().createBlockData(results[1]));
						if (!results[2].isEmpty()) {
							NBTExtractor nbt = new NBTExtractor();
							nbt.setNBT(b, results[2]);
						}
					}
					doneOperations++;
					GlobalVars.currentUndo = null;
				}
				
				// Selection move/stack
				else if (op.key.equalsIgnoreCase("selclone")) {
					Player tempPlayer = Operator.currentPlayer;
					Operator.currentPlayer = op.player;
					Block b = op.blocks.getNext();
					Operator.currentPlayer = tempPlayer;
					if (op.undo != null) {
						if (!op.undoRunning) {
							op.undo.startUndo();
						}
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
					// Actually do the clone
					for (int timesDone = 0; timesDone < op.times; timesDone++) {
						Block toEdit = b.getRelative(op.offset[0] * (1 + timesDone), op.offset[1] * (1 + timesDone), op.offset[2] * (1 + timesDone));
						SetBlock.setMaterial(toEdit, b.getType(), false);
						toEdit.setBlockData(b.getBlockData(), false);
						NBTExtractor nbt = new NBTExtractor();
						String nbtStr = nbt.getNBT(b);
						if (nbtStr.length() > 2) {
							String command = "data merge block " + toEdit.getX() + " " + toEdit.getY() + " " + toEdit.getZ() + " " + nbtStr;
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
							doneOperations++;
						}
					}
					if (op.delOriginal) {
						SetBlock.setMaterial(b, Material.AIR, false);
						doneOperations++;
					}
					doneOperations += op.times;
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
