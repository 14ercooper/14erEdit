package com._14ercooper.worldeditor.undo

import com._14ercooper.worldeditor.main.Main
import com._14ercooper.worldeditor.main.NBTExtractor
import com._14ercooper.worldeditor.main.SetBlock.setMaterial
import net.jpountz.lz4.LZ4Factory
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.BlockState
import java.nio.file.Files
import java.nio.file.Path

class UndoElement
    (id: String, parent: UserUndo) {

    // What's our state?
    var currentState = UndoMode.IDLE
        private set
    var userUndo : UserUndo = parent
    val name : String = id

    // Track blocks
    private var blockSizes : MutableList<String> = mutableListOf()

    // Block variables
    var data : MutableList<String> = mutableListOf()

    // Create an undo element if the ID doesn't exist, otherwise load the existing undo
    init {
        if (Files.exists(Path.of("plugins/14erEdit/undo/${parent.name}/$name"))) {
            loadFromDisk()
            Main.logDebug("Loaded undo element $name from disk for user ${parent.name}")
        }
        else {
            Files.createDirectories(Path.of("plugins/14erEdit/undo/${parent.name}/$name"))
            serialize()
            Main.logDebug("Created new undo element $name for user ${parent.name}")
        }
    }

    // Add a block to the undo
    fun addBlock(blockFrom : BlockState, blockTo : BlockState) : Boolean {
        if (currentState == UndoMode.IDLE)
            currentState = UndoMode.WAITING_FOR_BLOCKS
        val str = blockFrom.world.name + "\t" + blockFrom.x.toString() + "\t" + blockFrom.y.toString() + "\t" + blockFrom.z.toString() + "\t" + blockFrom.type.toString() + "\t" + blockFrom.blockData.asString + "\t" + nbtE.getNBT(blockFrom) + "\t" + blockTo.type.toString() + "\t" + blockTo.blockData.asString + "\t" + nbtE.getNBT(blockTo)
        data.add(str)
        if (data.size > blockSize) {
            serializeBlock()
        }
        return true
    }

    // Serialize block of data to disk
    private fun serializeBlock() : Boolean {
        val blockId = blockSizes.size
        val toSerialize = data.joinToString("\n").toByteArray(Charsets.UTF_8)
        val serializeLength = toSerialize.size
        blockSizes.add(serializeLength.toString())
        val compressor = factory.highCompressor(compressionLevel)
        val compressed = compressor.compress(toSerialize)
        Files.write(Path.of("plugins/14erEdit/undo/${userUndo.name}/$name/$blockId"), compressed)
        Main.logDebug("Serialized undo block $blockId for undo $name for user ${userUndo.name} with length $serializeLength")
        return true
    }

    // Load a block of data from disk
    private fun loadBlock(id : Int) : List<String> {
        val decompLen = blockSizes[id].toInt()
        val compressedBytes = Files.readAllBytes(Path.of("plugins/14erEdit/undo/${userUndo.name}/$name/$id"))
        val decompressor = factory.fastDecompressor()
        val decompBytes = decompressor.decompress(compressedBytes, decompLen)
        val decompStr = decompBytes.toString(Charsets.UTF_8)
        Main.logDebug("Loaded undo block $id for undo $name for user ${userUndo.name} with length $decompLen")
        return decompStr.lines()
    }

    // Serialize this undo to disk
    private fun serialize() : Boolean {
        serializeBlock()
        Files.writeString(Path.of("plugins/14erEdit/undo/${userUndo.name}/$name/blockSizes"), blockSizes.joinToString("\\"))
        val numBlocks = blockSizes.size
        Main.logDebug("Serialized undo element $name for user ${userUndo.name} to disk with $numBlocks blocks")
        return true
    }

    // Load this undo from disk
    private fun loadFromDisk() : Boolean {
        blockSizes = Files.readString(Path.of("plugins/14erEdit/undo/${userUndo.name}/$name/blockSizes")).split("\\") as MutableList<String>
        val numBlocks = blockSizes.size
        Main.logDebug("Loaded undo element $name for user ${userUndo.name} from disk with $numBlocks blocks")
        return true
    }

    // Track where we are in this undo (since there's data everywhere)
    private var currBlock = -1
    private var dataBlockCount = 0
    private var currData : MutableList<String> = mutableListOf()

    // Start applying the undo
    fun startApplyUndo() : Boolean {
        currBlock = -1
        dataBlockCount = blockSizes.size
        currData = mutableListOf()
        currentState = UndoMode.PERFORMING_UNDO
        Main.logDebug("Started applying undo $name for user ${userUndo.name}")
        return true
    }

    // Apply a number of blocks from the undo to the world
    fun applyUndo(blockCount : Long) : Boolean {
        if (currentState != UndoMode.PERFORMING_UNDO)
            return false
        var remBlocks = blockCount
        while (remBlocks > 0) {
            if (currData.size == 0) {
                // Load next block, or mark as done and return
                currBlock++
                if (currBlock == dataBlockCount) {
                    currentState = UndoMode.UNDO_FINISHED
                    return true
                }
                currData = mutableListOf()
                currData.addAll(loadBlock(currBlock))
            }
            // Pull data from block and set into world
            if (currData.isEmpty()) {
                Main.logDebug("Data in block $currBlock is empty, continuing")
                continue
            }
            val s = currData.removeAt(currData.lastIndex)
            val nextBlockData = s.split("\t")
            if (s.length >= 10)
                setBlock(nextBlockData[0], nextBlockData[1].toInt(), nextBlockData[2].toInt(), nextBlockData[3].toInt(), nextBlockData[4], nextBlockData[5], nextBlockData[6])
            remBlocks--
        }
        return true
    }

    // Check if this undo is finished being applied
    // If it is done, before returning true, clean up, else return false
    fun finalizeUndo() : Boolean {
        Main.logDebug("Finalized applying undo $name for user ${userUndo.name}")
        return currentState == UndoMode.UNDO_FINISHED
    }

    // Start applying a redo
    fun startApplyRedo() : Boolean {
        currBlock = -1
        dataBlockCount = blockSizes.size
        currData = mutableListOf()
        currentState = UndoMode.PERFORMING_REDO
        Main.logDebug("Started applying redo $name for user ${userUndo.name}")
        return true
    }

    // Apply a number of blocks from the redo to the world
    fun applyRedo(blockCount : Long) : Boolean {
        if (currentState != UndoMode.PERFORMING_REDO)
            return false
        var remBlocks = blockCount
        while (remBlocks > 0) {
            if (currData.size == 0) {
                // Load next block, or mark as done and return
                currBlock++
                if (currBlock == dataBlockCount) {
                    currentState = UndoMode.REDO_FINISHED
                    return true
                }
                currData = mutableListOf()
                currData.addAll(loadBlock(currBlock))
            }
            // Pull data from block and set into world
            if (currData.isEmpty()) {
                Main.logDebug("Data in block $currBlock is empty, continuing")
                continue
            }
            val s = currData.removeAt(currData.lastIndex)
            val nextBlockData = s.split("\t")
            if (s.length >= 10)
                setBlock(nextBlockData[0], nextBlockData[1].toInt(), nextBlockData[2].toInt(), nextBlockData[3].toInt(), nextBlockData[7], nextBlockData[8], nextBlockData[9])
            remBlocks--
        }
        return true
    }

    // Check if this redo is finished being applied
    // If it is done, before returning true, clean up, else return false
    fun finalizeRedo() : Boolean {
        Main.logDebug("Finalized applying redo $name for user ${userUndo.name}")
        return currentState == UndoMode.REDO_FINISHED
    }

    // Set a block into the world
    fun setBlock(world : String, x : Int, y : Int, z : Int, type : String, data : String, nbt : String) : Boolean {
        val blk = Bukkit.getServer().getWorld(world)?.getBlockAt(x, y, z)
        return if (blk != null) {
            setMaterial(blk, Material.matchMaterial(type), false, this)
            blk.blockData = Bukkit.getServer().createBlockData(data)
            if (nbt.isNotEmpty()) {
                val command = ("data merge block " + x + " " + y + " " + z + " "
                        + nbt)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
            }
            true
        } else {
            false
        }
    }

    // Flush all data to disk
    fun flush() : Boolean {
        if (currentState == UndoMode.PERFORMING_UNDO) {
            applyUndo(Long.MAX_VALUE)
            finalizeUndo()
        }
        if (currentState == UndoMode.PERFORMING_REDO) {
            applyRedo(Long.MAX_VALUE)
            finalizeRedo()
        }
        serialize()
        Main.logDebug("Flushed undo element $name for user ${userUndo.name}")
        return true
    }

    // Static vars
    companion object {
        const val blockSize = 1 shl 16
        const val compressionLevel = 12
        val factory: LZ4Factory = LZ4Factory.fastestInstance()
        val nbtE = NBTExtractor()
    }
}