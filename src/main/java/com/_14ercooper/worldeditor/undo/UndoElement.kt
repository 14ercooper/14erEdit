package com._14ercooper.worldeditor.undo

import org.bukkit.block.Block
import org.bukkit.block.BlockState

class UndoElement {

    // Create an undo element if the ID doesn't exist, otherwise load the existing undo
    constructor(id : String? = null) {

    }

    // What's our state?
    var currentState = UndoMode.IDLE
        private set
    var userUndo : UserUndo = UserUndo()

    // Add a block to the undo
    fun addBlock(blockFrom : BlockState, blockTo : BlockState) : Boolean {
        return false
    }

    // Serialize this undo to disk
    fun serialize() : Boolean {
        return false
    }

    // Start applying the undo
    fun startApplyUndo() : Boolean {
        return false
    }

    // Apply a number of blocks from the undo to the world
    fun applyUndo(blockCount : Long) : Boolean {
        return false
    }

    // Check if this undo is finished being applied
    // If it is done, before returning true, clean up, else return false
    fun finalizeUndo() : Boolean {
        return false
    }

    // Start applying a redo
    fun startApplyRedo() : Boolean {
        return false
    }

    // Apply a number of blocks from the redo to the world
    fun applyRedo(blockCount : Long) : Boolean {
        return false
    }

    // Check if this redo is finished being applied
    // If it is done, before returning true, clean up, else return false
    fun finalizeRedo() : Boolean {
        return false
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
        return serialize()
    }
}