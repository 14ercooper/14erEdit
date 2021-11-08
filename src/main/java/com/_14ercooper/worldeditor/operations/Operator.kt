package com._14ercooper.worldeditor.operations

import com._14ercooper.worldeditor.async.AsyncManager
import com._14ercooper.worldeditor.main.Main.Companion.logError
import com._14ercooper.worldeditor.operations.operators.core.EntryNode
import org.bukkit.command.CommandSender

class Operator {
    val entryNode: EntryNode

    fun operateOnBlock(state: OperatorState) : Boolean {
        return try {
            // Perform the operation
            entryNode.performNode(state)
        } catch (e: Exception) {
            logError("Could not perform operation. Please check your syntax.", state.currentPlayer, e)
            AsyncManager.dropAsync()
            false
        }
    }

    constructor(op: String, p: CommandSender) {
        entryNode = Parser.parseOperation(p, op)!!
    }

    constructor(e: EntryNode) {
        entryNode = e
    }
}