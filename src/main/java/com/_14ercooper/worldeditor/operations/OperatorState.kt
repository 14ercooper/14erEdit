package com._14ercooper.worldeditor.operations

import com._14ercooper.worldeditor.undo.UndoElement
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.command.CommandSender

open class OperatorState(
    var currentBlock: Block,
    var currentPlayer: CommandSender,
    var currentWorld: World,
    var currentUndo: UndoElement
) {
    var ignoringPhysics = false
}