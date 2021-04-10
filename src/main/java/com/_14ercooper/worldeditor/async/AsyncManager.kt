package com._14ercooper.worldeditor.async

import com._14ercooper.schematics.SchemLite
import com._14ercooper.worldeditor.blockiterator.BlockIterator
import com._14ercooper.worldeditor.blockiterator.iterators.SchemBrushIterator
import com._14ercooper.worldeditor.main.GlobalVars
import com._14ercooper.worldeditor.main.Main
import com._14ercooper.worldeditor.main.NBTExtractor
import com._14ercooper.worldeditor.main.SetBlock.setMaterial
import com._14ercooper.worldeditor.operations.Operator
import com._14ercooper.worldeditor.undo.UndoManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.IOException
import java.util.*

class AsyncManager {
    // Flag queue dropped
    private var queueDropped = false

    // Store operations
    private var operations = ArrayList<AsyncOperation>()
    private var queuedOperations = ArrayList<AsyncOperation>()

    // Store large operations
    private var largeOps: Queue<AsyncOperation> = ArrayDeque()

    // Drops the async queue
    fun dropAsync() {
        Main.logDebug("Async queue dropped.")
        for (asyncOp in operations) {
            if (asyncOp.undo != null && asyncOp.undoRunning) asyncOp.undo!!.finishUndo()
        }
        operations = ArrayList()
        queuedOperations = ArrayList()
        largeOps = ArrayDeque()
        queueDropped = true
        doneOperations += (Int.MAX_VALUE / 2).toLong()
    }

    // How many blocks do we have less
    val remainingBlocks: Long
        get() {
            var remBlocks: Long = 0
            remBlocks = getRemBlocks(remBlocks, operations)
            remBlocks = getRemBlocks(remBlocks, queuedOperations)
            return remBlocks
        }

    private fun getRemBlocks(remBlocks: Long, ops: List<AsyncOperation>): Long {
        var remBlocksSum = remBlocks
        for (a in ops) {
            if (a.blocks != null) {
                remBlocksSum += a.blocks!!.remainingBlocks
            } else {
                remBlocksSum += 100
            }
        }
        return remBlocksSum
    }

    // About how big is the async queue?
    fun asyncProgress(p: CommandSender) {
        val remBlocks = remainingBlocks
        val remTime = (remBlocks / (GlobalVars.blocksPerAsync * (20.0 / GlobalVars.ticksPerAsync))).toInt()
        p.sendMessage(
            "§aThere are " + remBlocks
                    + " blocks in the async queue, for an estimated remaining time of less than " + remTime + " seconds."
        )
    }

    // Dump some data about what's in the async queue
    fun asyncDump(p: CommandSender) {
        p.sendMessage(
            "§aThere are currently " + (operations.size + queuedOperations.size) + " operations in the async queue for "
                    + remainingBlocks + " blocks."
        )
        p.sendMessage("§aThe large edits confirm queue has " + largeOps.size + " pending operations.")
        p.sendMessage("§aPer Operation Stats:")
        for (a in queuedOperations) {
            p.sendMessage(
                "§a[Queued] " + a.blocks!!.remainingBlocks + " blocks remaining out of "
                        + a.blocks!!.totalBlocks + " total blocks. Undo? " + a.undoRunning + " " + a.blocks.toString()
            )
        }
        for (a in operations) {
            p.sendMessage(
                "§a[Running] " + a.blocks!!.remainingBlocks + " blocks remaining out of "
                        + a.blocks!!.totalBlocks + " total blocks. Undo? " + a.undoRunning + " " + a.blocks.toString()
            )
        }
        for (a in largeOps) {
            p.sendMessage("§a[Large Queue] " + a.blocks!!.totalBlocks + " total blocks. " + a.blocks.toString())
        }
    }

    // Schedule a block iterator task
    fun scheduleEdit(o: Operator?, p: Player?, b: BlockIterator) {
        if (p == null) {
            queuedOperations.add(AsyncOperation(o, b))
        } else if (b.totalBlocks > GlobalVars.undoLimit) {
            largeOps.add(AsyncOperation(o, p, b))
            if (!GlobalVars.autoConfirm) {
                p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.")
                p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel")
            }
        } else {
            val asyncOp = AsyncOperation(o, p, b)
            asyncOp.undo = UndoManager.getUndo(p)
            queuedOperations.add(asyncOp)
        }
    }

    // Schedule a nested block iterator task
    fun scheduleEdit(o: Operator?, p: Player?, b: BlockIterator, force: Boolean) {
        if (!force) {
            scheduleEdit(o, p, b)
            return
        }
        when {
            p == null -> queuedOperations.add(AsyncOperation(o, b))
            b.totalBlocks > GlobalVars.undoLimit -> {
                val asyncOp = AsyncOperation(o, p, b)
                queuedOperations.add(asyncOp)
            }
            else -> {
                val asyncOp = AsyncOperation(o, p, b)
                asyncOp.undo = UndoManager.getUndo(p)
                queuedOperations.add(asyncOp)
            }
        }
    }

    // Schedule a multibrush operation
    fun scheduleEdit(iterators: List<BlockIterator>, operations: List<Operator>, p: Player) {
        val asyncOp = AsyncOperation(iterators, operations, p)
        largeOps.add(asyncOp)
        if (!GlobalVars.autoConfirm) {
            p.sendMessage("§aMultibrushes can only be run in large edit mode without undos.")
            p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel")
        }
    }

    // Schedule a schematics operation
    fun scheduleEdit(sl: SchemLite?, saveSchem: Boolean, p: Player, origin: IntArray) {
        val asyncOp = AsyncOperation(sl, saveSchem, origin, p)
        if (asyncOp.blocks!!.totalBlocks > GlobalVars.undoLimit) {
            largeOps.add(asyncOp)
            if (!GlobalVars.autoConfirm) {
                p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.")
                p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel")
            }
        } else {
            asyncOp.undo = UndoManager.getUndo(p)
            queuedOperations.add(asyncOp)
        }
    }

    // Schedule a selection clone operation
    fun scheduleEdit(b: BlockIterator?, offset: IntArray, times: Int, delOriginal: Boolean, p: Player) {
        val asyncOp = AsyncOperation(b, offset, times, delOriginal, p)
        if (asyncOp.blocks!!.totalBlocks * times > GlobalVars.undoLimit) {
            largeOps.add(asyncOp)
            if (!GlobalVars.autoConfirm) {
                p.sendMessage("§aThis is a large edit that cannot be undone, and may stall 14erEdit for a while.")
                p.sendMessage("§aPlease type §b/confirm §ato confirm or §b/cancel §a to cancel")
            }
        } else {
            asyncOp.undo = UndoManager.getUndo(p)
            queuedOperations.add(asyncOp)
        }
    }

    // Confirm large edits
    fun confirmEdits(number: Int) {
        var numberConfirm = number
        numberConfirm = if (numberConfirm < 1) 1 else numberConfirm.coerceAtMost(largeOps.size)
        Main.logDebug("Confirming $numberConfirm large edits.")
        while (numberConfirm-- > 0) {
            queuedOperations.add(largeOps.remove())
        }
    }

    // Cancel large edits
    fun cancelEdits(number: Int) {
        var numberCancel = number
        numberCancel = if (numberCancel < 1) 1 else numberCancel.coerceAtMost(largeOps.size)
        Main.logDebug("Cancelling $numberCancel large edits.")
        while (numberCancel-- > 0) {
            largeOps.remove()
        }
    }

    // Scheduled task to operate
    private fun performOperation() {
        // Output building up debug
        Main.outputDebug()

        // If in autoconfirm mode, do that
        if (GlobalVars.autoConfirm) {
            confirmEdits(10)
        }

        // Dispatch async edits
        if (operations.size < 10) {
            var i = 0
            while (i < queuedOperations.size) {
                if (queuedOperations[i].undo == null || queuedOperations[i].undo!!.canStartUndo(queuedOperations[i].blocks!!.totalBlocks)) {
                    if (queuedOperations[i].undo != null) {
                        queuedOperations[i].undo!!.startTrackingUndo(queuedOperations[i].blocks!!.totalBlocks)
                    }
                    operations.add(queuedOperations[i])
                    queuedOperations.removeAt(i)
                    i--
                }
                if (operations.size >= 10) {
                    break
                }
                i++
            }
        }

        // If there isn't anything to do, return
        if (operations.size == 0) return

        // Limit operations per run
        doneOperations = 0
        if (queueDropped) {
            queueDropped = false
        }
        GlobalVars.errorLogged = false

        // Loop until finished
        while (doneOperations < GlobalVars.blocksPerAsync && operations.size > 0) {
            if (queueDropped) {
                queueDropped = false
                return
            }
            var opSize = operations.size
            var i = 0
            while (i < opSize) {
                if (queueDropped) {
                    queueDropped = false
                    return
                }
                val currentAsyncOp = operations[i]
                if (currentAsyncOp.blocks != null && currentAsyncOp.blocks!!.remainingBlocks == 0L) {
                    if (currentAsyncOp.undo != null) {
                        currentAsyncOp.undo!!.finishUndo()
                    }
                    if (currentAsyncOp.key.equals("saveschem", ignoreCase = true)) {
                        currentAsyncOp.player!!.sendMessage("§aSelection saved")
                    }
                    if (currentAsyncOp.blocks is SchemBrushIterator) {
                        (currentAsyncOp.blocks as SchemBrushIterator).cleanup()
                    }
                    operations.removeAt(i)
                    i--
                    opSize--
                    i++
                    continue
                }

                // Iterator edit
                if (currentAsyncOp.key.equals("iteredit", ignoreCase = true) || currentAsyncOp.key.equals(
                        "rawiteredit",
                        ignoreCase = true
                    )
                ) {
                    val b = getBlock(currentAsyncOp)
                    if (b == null) {
                        if (currentAsyncOp.undo != null) {
                            currentAsyncOp.undo!!.finishUndo()
                        }
                        if (currentAsyncOp.blocks is SchemBrushIterator) {
                            (currentAsyncOp.blocks as SchemBrushIterator).cleanup()
                        }
                        operations.removeAt(i)
                        i--
                        opSize--
                        i++
                        continue
                    }
                    currentAsyncOp.operation!!.operateOnBlock(b, currentAsyncOp.player)
                    doneOperations++
                    GlobalVars.currentUndo = null
                } else if (currentAsyncOp.key.equals("multibrush", ignoreCase = true)) {
                    var b: Block?
                    var doContinue = false
                    while (true) {
                        b = currentAsyncOp.iterators[0].next
                        if (b != null) {
                            break
                        }
                        if (currentAsyncOp.iterators.size > 1) {
                            currentAsyncOp.iterators.removeAt(0)
                            if (currentAsyncOp.iterators[0] is SchemBrushIterator) {
                                (currentAsyncOp.iterators[0] as SchemBrushIterator).cleanup()
                            }
                            currentAsyncOp.operations.removeAt(0)
                            if (currentAsyncOp.iterators.size == 0 || currentAsyncOp.operations.size == 0) {
                                doContinue = true
                                break
                            }
                        } else {
                            doContinue = true
                            break
                        }
                    }
                    if (doContinue) {
                        operations.removeAt(i)
                        i--
                        opSize--
                        i++
                        continue
                    }
                    currentAsyncOp.operations[0].operateOnBlock(b, currentAsyncOp.player)
                    doneOperations++
                    GlobalVars.currentUndo = null
                } else if (currentAsyncOp.key.equals("messyedit", ignoreCase = true)) {
                    operations.removeAt(i).operation!!.messyOperate()
                    i--
                    opSize--
                    doneOperations += 100
                } else if (currentAsyncOp.key.equals("saveschem", ignoreCase = true)) {
                    assert(currentAsyncOp.blocks != null)
                    val b = currentAsyncOp.blocks!!.next
                    if (currentAsyncOp.undo != null) {
                        if (!currentAsyncOp.undoRunning) {
                            currentAsyncOp.undo!!.startUndo(currentAsyncOp.blocks!!.totalBlocks)
                            try {
                                currentAsyncOp.schem!!.resetWrite()
                            } catch (e: IOException) {
                                Main.logError("Could not write to schematic file", currentAsyncOp.player, e)
                            }
                        }
                        GlobalVars.currentUndo = currentAsyncOp.undo
                        currentAsyncOp.undoRunning = true
                    }
                    if (b == null) {
                        if (currentAsyncOp.undo != null) {
                            currentAsyncOp.undo!!.finishUndo()
                        }
                        operations.removeAt(i)
                        i--
                        opSize--
                        currentAsyncOp.player!!.sendMessage("§aSelection saved")
                        i++
                        continue
                    }
                    val material = b.type.toString()
                    val data = b.blockData.asString
                    val nbtE = NBTExtractor()
                    val nbt = nbtE.getNBT(b)
                    try {
                        currentAsyncOp.schem!!.writeBlock(material, data, nbt)
                    } catch (e: IOException) {
                        // Don't need to do anything
                    }
                    doneOperations++
                    GlobalVars.currentUndo = null
                } else if (currentAsyncOp.key.equals("loadschem", ignoreCase = true)) {
                    val b = getBlock(currentAsyncOp)
                    if (b == null) {
                        if (currentAsyncOp.undo != null) {
                            currentAsyncOp.undo!!.finishUndo()
                        }
                        operations.removeAt(i)
                        i--
                        opSize--
                        try {
                            currentAsyncOp.schem!!.closeRead()
                        } catch (e: IOException) {
                            // Don't need to do anything
                        }
                        currentAsyncOp.player!!.sendMessage("§aData loaded")
                        i++
                        continue
                    }
                    var results = arrayOf<String>()
                    try {
                        results = currentAsyncOp.schem!!.readNext()
                    } catch (e: IOException) {
                        // Don't need to do anything
                    }
                    val m = Material.matchMaterial(results[0])
                    if (m != null && !m.isAir || currentAsyncOp.schem!!.setAir()) {
                        setMaterial(b, Material.matchMaterial(results[0]), false)
                        b.blockData = Bukkit.getServer().createBlockData(results[1])
                        if (results[2].isNotEmpty()) {
                            val command = ("data merge block " + b.x + " " + b.y + " " + b.z + " "
                                    + results[2])
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
                        }
                    }
                    doneOperations++
                    GlobalVars.currentUndo = null
                } else if (currentAsyncOp.key.equals("selclone", ignoreCase = true)) {
                    val tempPlayer = Operator.currentPlayer
                    Operator.currentPlayer = currentAsyncOp.player
                    assert(currentAsyncOp.blocks != null)
                    val b = currentAsyncOp.blocks!!.next
                    Operator.currentPlayer = tempPlayer
                    if (currentAsyncOp.undo != null) {
                        if (!currentAsyncOp.undoRunning) {
                            currentAsyncOp.undo!!.startUndo(currentAsyncOp.blocks!!.totalBlocks)
                        }
                        GlobalVars.currentUndo = currentAsyncOp.undo
                        currentAsyncOp.undoRunning = true
                    }
                    if (b == null) {
                        if (currentAsyncOp.undo != null) {
                            currentAsyncOp.undo!!.finishUndo()
                        }
                        operations.removeAt(i)
                        i--
                        opSize--
                        i++
                        continue
                    }
                    // Actually do the clone
                    for (timesDone in 0 until currentAsyncOp.times) {
                        val toEdit = b.getRelative(
                            currentAsyncOp.offset[0] * (1 + timesDone),
                            currentAsyncOp.offset[1] * (1 + timesDone), currentAsyncOp.offset[2] * (1 + timesDone)
                        )
                        setMaterial(toEdit, b.type, false)
                        toEdit.setBlockData(b.blockData, false)
                        val nbt = NBTExtractor()
                        val nbtStr = nbt.getNBT(b)
                        if (nbtStr.length > 2) {
                            val command = ("data merge block " + toEdit.x + " " + toEdit.y + " "
                                    + toEdit.z + " " + nbtStr)
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command)
                            doneOperations++
                        }
                    }
                    if (currentAsyncOp.delOriginal) {
                        setMaterial(b, Material.AIR, false)
                        doneOperations++
                    }
                    doneOperations += currentAsyncOp.times.toLong()
                    GlobalVars.currentUndo = null
                } else {
                    Main.logError(
                        "Invalid operation in async queue. Removing operation.",
                        Bukkit.getConsoleSender(),
                        null
                    )
                    operations.removeAt(i)
                    i--
                    opSize--
                }
                i++
            }
        }

        // Debug message
        if (doneOperations > 0) {
            Main.logDebug("Performed $doneOperations async operations")
        }
    }

    private fun getBlock(currentAsyncOp: AsyncOperation): Block? {
        assert(currentAsyncOp.blocks != null)
        val b = currentAsyncOp.blocks!!.next
        if (currentAsyncOp.undo != null) {
            if (!currentAsyncOp.undoRunning) {
                currentAsyncOp.undo!!.startUndo(currentAsyncOp.blocks!!.totalBlocks)
            }
            GlobalVars.currentUndo = currentAsyncOp.undo
            currentAsyncOp.undoRunning = true
        }
        return b
    }

    companion object {
        var doneOperations: Long = 0
    }

    // On initialization start the scheduler
    init {
        val scheduler = Bukkit.getServer().scheduler
        scheduler.scheduleSyncRepeatingTask(
            GlobalVars.plugin,
            { GlobalVars.asyncManager.performOperation() },
            GlobalVars.ticksPerAsync,
            GlobalVars.ticksPerAsync
        )
    }
}