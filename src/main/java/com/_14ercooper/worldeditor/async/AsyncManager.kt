/**
 * This file is part of 14erEdit.
 *
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.worldeditor.async

import com._14ercooper.schematics.SchemLite
import com._14ercooper.worldeditor.blockiterator.BlockIterator
import com._14ercooper.worldeditor.blockiterator.BlockWrapper
import com._14ercooper.worldeditor.blockiterator.iterators.SchemBrushIterator
import com._14ercooper.worldeditor.main.Main
import com._14ercooper.worldeditor.main.NBTExtractor
import com._14ercooper.worldeditor.main.SetBlock.setMaterial
import com._14ercooper.worldeditor.operations.Operator
import com._14ercooper.worldeditor.operations.OperatorState
import com._14ercooper.worldeditor.undo.UndoElement
import com._14ercooper.worldeditor.undo.UndoMode
import com._14ercooper.worldeditor.undo.UndoSystem
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.IOException
import java.lang.IndexOutOfBoundsException
import java.lang.NullPointerException

object AsyncManager {
    var blocksPerAsync: Long = 10000
    var ticksPerAsync: Long = 4
    var countEdits = false
    private const val opBlockSize = 1

    // Flag queue dropped
    private var queueDropped = false

    // Store operations
    private var operations: MutableList<AsyncOperation> = mutableListOf()
    private var queuedOperations: MutableList<AsyncOperation> = mutableListOf()

    // How much work have we done?
    var doneOperations: Long = 0


    // Get undo for commandsender
    private fun startUndo(sender: CommandSender?): UndoElement {
        return UndoSystem.findUserUndo(sender).getNewUndoElement()
    }

    // Finish an undo element
    private fun finishUndo(undo: UndoElement): Boolean {
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
        val remTime = (remBlocks / (blocksPerAsync * (20.0 / ticksPerAsync))).toInt()
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
    fun scheduleEdit(o: Operator?, p: CommandSender, b: BlockIterator, ue: UndoElement) {
        queuedOperations.add(AsyncOperation(o, p, b, ue))
    }

    // Schedule a multibrush operation
    @JvmStatic
    fun scheduleEdit(iterators: MutableList<out BlockIterator>, operations: MutableList<Operator>, p: CommandSender) {
        queuedOperations.add(AsyncOperation(iterators, operations, p, startUndo(p)))
    }

    @JvmStatic
    fun scheduleEdit(
        iterators: MutableList<out BlockIterator>,
        operations: MutableList<Operator>,
        p: CommandSender,
        ue: UndoElement
    ) {
        queuedOperations.add(AsyncOperation(iterators, operations, p, ue))
    }

    // Schedule a schematics operation
    @JvmStatic
    fun scheduleEdit(sl: SchemLite?, saveSchem: Boolean, p: Player, origin: IntArray) {
        queuedOperations.add(AsyncOperation(sl, saveSchem, origin, p, startUndo(p)))
    }

    // Schedule a schematics operation
    @JvmStatic
    fun scheduleEdit(sl: SchemLite?, saveSchem: Boolean, p: Player, origin: IntArray, undo : UndoElement) {
        queuedOperations.add(AsyncOperation(sl, saveSchem, origin, p, undo))
    }

    // Schedule a selection clone operation
    @JvmStatic
    fun scheduleEdit(b: BlockIterator?, offset: IntArray, times: Int, delOriginal: Boolean, p: Player) {
        queuedOperations.add(AsyncOperation(b, offset, times, delOriginal, p, startUndo(p)))
    }

    // Scheule an undo operation
    @JvmStatic
    fun scheduleEdit(undos: MutableList<UndoElement>, player: CommandSender) {
        queuedOperations.add(AsyncOperation(undos, player))
    }

    @JvmStatic
    fun incrementDoneOperations(amount: Long) {
        doneOperations += amount
    }

    // Scheduled task to operate
    private lateinit var currentAsyncOp: AsyncOperation
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

        // Loop until finished
        while (doneOperations < blocksPerAsync && operations.size > 0) {
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
                    val currWorld = if (currentAsyncOp.player is Player) {
                        (currentAsyncOp.player as Player).world
                    } else {
                        Main.plugin!!.server.worlds[0]
                    }

                    val tempState = OperatorState(BlockWrapper(currWorld.getBlockAt(14, 14, 14), 14, 14, 14), currentAsyncOp.player, currWorld, currentAsyncOp.undo,
                    Location(currWorld, 14.0, 14.0, 14.0))
                    currentAsyncOp.blocks!!.setObjectArgs("OperatorState", tempState);
                    val b = getBlock(currentAsyncOp)
                    if (b.isEmpty()) {
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

                    b.stream().forEach {
                        val state = OperatorState(it, currentAsyncOp.player, currWorld, currentAsyncOp.undo, Location(
                            currWorld,
                            currentAsyncOp.blocks!!.originX.toDouble(),
                            currentAsyncOp.blocks!!.originY.toDouble(), currentAsyncOp.blocks!!.originZ.toDouble()
                        )
                        )
                        currentAsyncOp.operation!!.operateOnBlock(state)
                    }
                    doneOperations += b.size
                } else if (currentAsyncOp.key.equals("undoedit", ignoreCase = true)) {
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
                            val name =
                                if (currentAsyncOp.undoList == null) "null" else currentAsyncOp.undoList!![0].name
                            Main.logDebug("Undo $name finalized")
                        } else {
                            currentAsyncOp.undoList!![0].finalizeRedo()
                            val name =
                                if (currentAsyncOp.undoList == null) "null" else currentAsyncOp.undoList!![0].name
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
                    var b: BlockWrapper?
                    var originX = 14
                    var originY = 14
                    var originZ = 14

                    while (true) {
                        if (currentAsyncOp.iterators.size == 0) {
                            b = null
                            break
                        }

                        try {
                            b = currentAsyncOp.iterators[0].getNextBlock(currentAsyncOp.player, true)
                            originX = currentAsyncOp.iterators[0].originX
                            originY = currentAsyncOp.iterators[0].originY
                            originZ = currentAsyncOp.iterators[0].originZ
                        } catch (e: IndexOutOfBoundsException) {
                            Main.logError("Multibrush error: no iterators (does the file exist?)", currentAsyncOp.player, e)
                            dropAsync()
                            return
                        }

                        if (b == null) {
                            if (currentAsyncOp.iterators[0] is SchemBrushIterator) {
                                (currentAsyncOp.iterators[0] as SchemBrushIterator).cleanup()
                            }
                            currentAsyncOp.iterators.removeFirst()
                            currentAsyncOp.operations.removeFirst()
                        }
                        else {
                            break
                        }
                    }

                    if (b == null) {
                        finishUndo(currentAsyncOp.undo)
                        operations.removeAt(i)
                        i--
                        opSize--
                        i++
                        continue
                    }

                    val currWorld = if (currentAsyncOp.player is Player) {
                        (currentAsyncOp.player as Player).world
                    } else {
                        Main.plugin!!.server.worlds[0]
                    }
                    val state = OperatorState(b, currentAsyncOp.player, currWorld, currentAsyncOp.undo, Location(currWorld,
                        originX.toDouble(), originY.toDouble(), originZ.toDouble()
                    )
                    )
                    currentAsyncOp.operations[0].operateOnBlock(state)
                    doneOperations += 1
                }
                else if (currentAsyncOp.key.equals("saveschem", ignoreCase = true)) {
                    assert(currentAsyncOp.blocks != null)
                    val b = currentAsyncOp.blocks!!.getNext(1, currentAsyncOp.player, true)[0]
                    if (!currentAsyncOp.startedWrite) {
                        try {
                            currentAsyncOp.schem!!.resetWrite()
                            currentAsyncOp.startedWrite = true
                        } catch (e: IOException) {
                            Main.logError("Could not write to schematic file", currentAsyncOp.player, e)
                        }
                    }

                    val material = b.block.type.toString()
                    val data = b.block.blockData.asString
                    val nbtE = NBTExtractor()
                    val nbt = nbtE.getNBT(b.block)
                    try {
                        currentAsyncOp.schem!!.writeBlock(material, data, nbt)
                    } catch (e: IOException) {
                        // Don't need to do anything
                    }
                    doneOperations++
                } else if (currentAsyncOp.key.equals("loadschem", ignoreCase = true)) {
                    val b = currentAsyncOp.blocks!!.getNext(1, currentAsyncOp.player, true)[0]
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
                        setMaterial(
                            b.block,
                            Material.matchMaterial(results[0]),
                            false,
                            currentAsyncOp.undo,
                            currentAsyncOp.player
                        )
                        b.block.blockData = Bukkit.getServer().createBlockData(results[1])
                        if (results[2].isNotEmpty()) {
                            val command = ("data merge block " + b.x + " " + b.y + " " + b.z + " "
                                    + results[2])
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
                        }
                    }
                    doneOperations++
                } else if (currentAsyncOp.key.equals("selclone", ignoreCase = true)) {
                    assert(currentAsyncOp.blocks != null)
                    val b = currentAsyncOp.blocks!!.getNext(1, currentAsyncOp.player, true)[0]
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
                        val toEdit = b.block.getRelative(
                            currentAsyncOp.offset[0] * (1 + timesDone),
                            currentAsyncOp.offset[1] * (1 + timesDone), currentAsyncOp.offset[2] * (1 + timesDone)
                        )
                        setMaterial(toEdit, b.block.type, b.block.blockData, false, currentAsyncOp.undo, currentAsyncOp.player)
                        val nbt = NBTExtractor()
                        val nbtStr = nbt.getNBT(b.block)
                        if (nbtStr.length > 2) {
                            val command = ("data merge block " + toEdit.x + " " + toEdit.y + " "
                                    + toEdit.z + " " + nbtStr)
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command)
                            doneOperations++
                        }
                    }
                    if (currentAsyncOp.delOriginal) {
                        setMaterial(b.block, Material.AIR, false, currentAsyncOp.undo, currentAsyncOp.player)
                        doneOperations++
                    }
                    doneOperations += currentAsyncOp.times.toLong()
                } else {
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

    private fun getBlock(currentAsyncOp: AsyncOperation): List<BlockWrapper> {
        assert(currentAsyncOp.blocks != null)
        return currentAsyncOp.blocks!!.getNext(opBlockSize, currentAsyncOp.player, true)
    }

    // On initialization start the scheduler
    init {
        try {
            val scheduler = Bukkit.getServer().scheduler
            scheduler.scheduleSyncRepeatingTask(
                Main.plugin!!,
                { performOperation() },
                ticksPerAsync,
                ticksPerAsync
            )
        }
        catch (e : NullPointerException) {
            // Do nothing, this is a test
        }
    }
}