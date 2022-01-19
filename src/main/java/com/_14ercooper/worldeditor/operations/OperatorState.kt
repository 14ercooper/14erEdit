// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations

import com._14ercooper.worldeditor.blockiterator.BlockWrapper
import com._14ercooper.worldeditor.undo.UndoElement
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.command.CommandSender

open class OperatorState(
    var currentBlock: BlockWrapper,
    var currentPlayer: CommandSender,
    var currentWorld: World,
    var currentUndo: UndoElement,
    var originLocation: Location
) {
    var ignoringPhysics = false

    fun setCurrentBlock(b: Block) {
        currentBlock = BlockWrapper(b, b.x, b.y, b.z)
    }

    val otherValues : MutableMap<String, String> = mutableMapOf()
}