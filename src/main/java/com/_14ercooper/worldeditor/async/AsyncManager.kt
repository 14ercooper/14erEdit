package com._14ercooper.worldeditor.async

import com._14ercooper.schematics.SchemLite
import com._14ercooper.worldeditor.blockiterator.BlockIterator
import com._14ercooper.worldeditor.blockiterator.iterators.SchemBrushIterator
import com._14ercooper.worldeditor.main.GlobalVars
import com._14ercooper.worldeditor.main.Main
import com._14ercooper.worldeditor.main.NBTExtractor
import com._14ercooper.worldeditor.main.SetBlock.setMaterial
import com._14ercooper.worldeditor.operations.Operator
import com._14ercooper.worldeditor.undo.UndoElement
import com._14ercooper.worldeditor.undo.UndoMode
import com._14ercooper.worldeditor.undo.UndoSystem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.IOException

object AsyncManager {
    // Flag queue dropped
    private var queueDropped = false

    // Store operations
    private var operations : MutableList<AsyncOperation> = mutableListOf()
    private var queuedOperations : MutableList<AsyncOperation> = mutableListOf()

    // How much work have we done?
    var doneOperations: Long = 0


    // Get undo for commandsender
    private fun startUndo(sender : CommandSender?) : UndoElement {
        return UndoSystem.findUserUndo(sender).getNewUndoElement()
    }

    // Finish an undo element
    private fun finishUndo(undo : UndoElement) : Boolean {
        return if (undo.currentState == UndoMode.WAITING_FOR_BLOCKS)
            undo.userUndo.finalizeUndo(undo)
        else true
    }

    // Drops the async queue
    @JvmStatic
    fun dropAsync() {
        Main.logDebug("Async queue dropped.")
        for (asyncOp in operations) {
            if (asyncOp.undo.currentState == UndoMode.WAITING_FOR_BLOCKS) finishUndo(asyncOp.undo)
        }
        for (asyncOp in queuedOperations) {
            if (asyncOp.undo.currentState == UndoMode.WAITING_FOR_BLOCKS) finishUndo(asyncOp.undo)
        }
        operations = mutableListOf()
        queuedOperations = mutableListOf()
        queueDropped = true
        doneOperations += (Int.MAX_VALUE / 2).toLong()
    }

    // How many blocks do we have less
    @JvmStatic
    val remainingBlocks: Long
        get() {
            var remBlocks: Long = 0
            remBlocks = getRemBlocks(remBlocks, operations)
            remBlocks = getRemBlocks(remBlocks, queuedOperations)
            return remBlocks
        }

    @JvmStatic
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
    @JvmStatic
    fun asyncProgress(p: CommandSender) {
        val remBlocks = remainingBlocks
        val remTime = (remBlocks / (GlobalVars.blocksPerAsync * (20.0 / GlobalVars.ticksPerAsync))).toInt()
        p.sendMessage(
            "§aThere are " + remBlocks
                    + " blocks in the async queue, for an estimated remaining time of less than " + remTime + " seconds."
        )
    }

    // Dump some data about what's in the async queue
    @JvmStatic
    fun asyncDump(p: CommandSender) {
        p.sendMessage(
            "§aThere are currently " + (operations.size + queuedOperations.size) + " operations in the async queue for "
                    + remainingBlocks + " blocks."
        )
        p.sendMessage("§aPer Operation Stats:")
        for (a in queuedOperations) {
            p.sendMessage(
                "§a[Queued] " + a.blocks?.remainingBlocks + " blocks remaining out of "
                        + a.blocks?.totalBlocks + " total blocks. Undo? " + a.undo.currentState + " " + a.blocks.toString()
            )
        }
        for (a in operations) {
            p.sendMessage(
                "§a[Running] " + a.blocks?.remainingBlocks + " blocks remaining out of "
                        + a.blocks?.totalBlocks + " total blocks. Undo? " + a.undo.currentState + " " + a.blocks.toString()
            )
        }
    }

    // Schedule a block iterator task
    @JvmStatic
    fun scheduleEdit(o: Operator?, p: CommandSender, b: BlockIterator) {
        queuedOperations.add(AsyncOperation(o, p, b, startUndo(p)))
    }
    @JvmStatic
    fun scheduleEdit(o: Operator?, p: CommandSender, b: BlockIterator, ue : UndoElement) {
        queuedOperations.add(AsyncOperation(o, p, b, ue))
    }

    // Schedule a multibrush operation
    @JvmStatic
    fun scheduleEdit(iterators: MutableList<out BlockIterator>, operations: MutableList<Operator>, p: CommandSender) {
        queuedOperations.add(AsyncOperation(iterators, operations, p, startUndo(p)))
    }
    @JvmStatic
    fun scheduleEdit(iterators: MutableList<out BlockIterator>, operations: MutableList<Operator>, p: CommandSender, ue : UndoElement) {
        queuedOperations.add(AsyncOperation(iterators, operations, p, ue))
    }

    // Schedule a schematics operation
    @JvmStatic
    fun scheduleEdit(sl: SchemLite?, saveSchem: Boolean, p: Player, origin: IntArray) {
        queuedOperations.add(AsyncOperation(sl, saveSchem, origin, p, startUndo(p)))
    }

    // Schedule a selection clone operation
    @JvmStatic
    fun scheduleEdit(b: BlockIterator?, offset: IntArray, times: Int, delOriginal: Boolean, p: Player) {
        queuedOperations.add(AsyncOperation(b, offset, times, delOriginal, p, startUndo(p)))
    }

    // Scheule an undo operation
    @JvmStatic
    fun scheduleEdit(undos : MutableList<UndoElement>, player : CommandSender) {
        queuedOperations.add(AsyncOperation(undos, player))
    }

    // Scheduled task to operate
    lateinit var currentAsyncOp : AsyncOperation
    private fun performOperation() {
        // Output building up debug
        Main.outputDebug()

        // Dispatch async edits
        if (operations.size < 10) {
            val i = 0
            while (i < queuedOperations.size) {
                if (operations.size >= 10) {
                    break
                }
                operations.add(queuedOperations[i])
                queuedOperations.removeAt(i)
            }
        }

        // If there isn't anything to do, return
        if (operations.size == 0) {
            if (!UndoSystem.isFlushed()) {
                UndoSystem.flush()
            }
            return
        }

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
                currentAsyncOp = operations[i]
                if (currentAsyncOp.blocks != null && currentAsyncOp.blocks!!.remainingBlocks == 0L) {
                    finishUndo(currentAsyncOp.undo)
                    if (currentAsyncOp.key.equals("saveschem", ignoreCase = true)) {
                        currentAsyncOp.player.sendMessage("§aSelection saved")
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
                        finishUndo(currentAsyncOp.undo)
                        if (currentAsyncOp.blocks is SchemBrushIterator) {
                            (currentAsyncOp.blocks as SchemBrushIterator).cleanup()
                        }
                        operations.removeAt(i)
                        i--
                        opSize--
                        i++
                        continue
                    }
                    currentAsyncOp.operation!!.operateOnBlock(b, currentAsyncOp.player, currentAsyncOp.undo)
                    doneOperations++
                }

                else if (currentAsyncOp.key.equals("undoedit", ignoreCase = true)) {
                    val undoBatchSize = 32L
                    // If undo or redo finished, handle it
                    if (currentAsyncOp.undoList!!.isEmpty()) {
                        operations.removeAt(i)
                        i--
                        opSize--
                        i++
                        continue
                    }
                    if (currentAsyncOp.undoList!![0].currentState == UndoMode.UNDO_FINISHED || currentAsyncOp.undoList!![0].currentState == UndoMode.REDO_FINISHED) {
                        if (currentAsyncOp.undoList!![0].currentState == UndoMode.UNDO_FINISHED) {
                            currentAsyncOp.undoList!![0].finalizeUndo()
                            val name = if (currentAsyncOp.undoList == null) "null" else currentAsyncOp.undoList!![0].name
                            Main.logDebug("Undo $name finalized")
                        }
                        else {
                            currentAsyncOp.undoList!![0].finalizeRedo()
                            val name = if (currentAsyncOp.undoList == null) "null" else currentAsyncOp.undoList!![0].name
                            Main.logDebug("Redo $name finalized")
                        }
                        currentAsyncOp.undoList!!.removeAt(0)
                        if (currentAsyncOp.undoList!!.size == 0) {
                            operations.removeAt(i)
                            i--
                            opSize--
                            currentAsyncOp.player.sendMessage("§aUndo operation finished")
                            Main.logDebug("All undos in set finished")
                            i++
                            continue
                        }
                    }
                    // If undo running, handle it
                    else if (currentAsyncOp.undoList!![0].currentState == UndoMode.PERFORMING_UNDO) {
                        currentAsyncOp.undoList!![0].applyUndo(undoBatchSize)
                        doneOperations += undoBatchSize
                    }
                    // If redo running, handle it
                    else if (currentAsyncOp.undoList!![0].currentState == UndoMode.PERFORMING_REDO) {
                        currentAsyncOp.undoList!![0].applyRedo(undoBatchSize)
                        doneOperations += undoBatchSize

                    }

                }

                else if (currentAsyncOp.key.equals("multibrush", ignoreCase = true)) {
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
                        finishUndo(currentAsyncOp.undo)
                        operations.removeAt(i)
                        i--
                        opSize--
                        i++
                        continue
                    }
                    currentAsyncOp.operations[0].operateOnBlock(b, currentAsyncOp.player, currentAsyncOp.undo)
                    doneOperations++
                }

                else if (currentAsyncOp.key.equals("saveschem", ignoreCase = true)) {
                    assert(currentAsyncOp.blocks != null)
                    val b = currentAsyncOp.blocks!!.next
                    if (!currentAsyncOp.startedWrite) {
                        try {
                            currentAsyncOp.schem!!.resetWrite()
                            currentAsyncOp.startedWrite = true
                        } catch (e: IOException) {
                            Main.logError("Could not write to schematic file", currentAsyncOp.player, e)
                        }
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
                }

                else if (currentAsyncOp.key.equals("loadschem", ignoreCase = true)) {
                    val b = getBlock(currentAsyncOp)
                    if (b == null) {
                        finishUndo(currentAsyncOp.undo)
                        operations.removeAt(i)
                        i--
                        opSize--
                        try {
                            currentAsyncOp.schem!!.closeRead()
                        } catch (e: IOException) {
                            // Don't need to do anything
                        }
                        currentAsyncOp.player.sendMessage("§aData loaded")
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
                        setMaterial(b, Material.matchMaterial(results[0]), false, currentAsyncOp.undo)
                        b.blockData = Bukkit.getServer().createBlockData(results[1])
                        if (results[2].isNotEmpty()) {
                            val command = ("data merge block " + b.x + " " + b.y + " " + b.z + " "
                                    + results[2])
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
                        }
                    }
                    doneOperations++
                }

                else if (currentAsyncOp.key.equals("selclone", ignoreCase = true)) {
                    val tempPlayer = Operator.currentPlayer
                    Operator.currentPlayer = currentAsyncOp.player
                    assert(currentAsyncOp.blocks != null)
                    val b = currentAsyncOp.blocks!!.next
                    Operator.currentPlayer = tempPlayer
                    if (b == null) {
                        finishUndo(currentAsyncOp.undo)
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
                        setMaterial(toEdit, b.type, false, currentAsyncOp.undo)
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
                        setMaterial(b, Material.AIR, false, currentAsyncOp.undo)
                        doneOperations++
                    }
                    doneOperations += currentAsyncOp.times.toLong()
                }

                else {
                    finishUndo(currentAsyncOp.undo)
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
        return currentAsyncOp.blocks!!.next
    }

    // On initialization start the scheduler
    init {
        val scheduler = Bukkit.getServer().scheduler
        scheduler.scheduleSyncRepeatingTask(
            GlobalVars.plugin,
            { performOperation() },
            GlobalVars.ticksPerAsync,
            GlobalVars.ticksPerAsync
        )
    }
}