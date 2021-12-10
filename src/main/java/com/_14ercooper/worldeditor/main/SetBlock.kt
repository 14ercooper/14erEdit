package com._14ercooper.worldeditor.main

import com._14ercooper.worldeditor.async.AsyncManager
import com._14ercooper.worldeditor.player.PlayerManager
import com._14ercooper.worldeditor.undo.UndoElement
import com._14ercooper.worldeditor.undo.UndoMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.Leaves
import org.bukkit.command.CommandSender

object SetBlock {
    @JvmStatic
    fun setMaterial(b: Block, mat: Material?, undo: UndoElement, currentPlayer: CommandSender): Boolean {
        if (AsyncManager.countEdits) {
            ++AsyncManager.doneOperations
        }
        if (PlayerManager.getPlayerWrapper(currentPlayer).outsideEditRegion(b.x.toLong(), b.y.toLong(), b.z.toLong())) {
            return false
        }
        if (!PlayerManager.getPlayerWrapper(currentPlayer).shouldEdit(b.type)) {
            return false
        }
        try {
            val bs = b.state
            b.setType(mat!!, false)
            if (undo.currentState != UndoMode.PERFORMING_UNDO && undo.currentState != UndoMode.PERFORMING_REDO)
                undo.addBlock(bs, b.state)
            updateLeafData(mat, b)
            return true
        } catch (e: Exception) {
            invalidMaterial(mat, e, currentPlayer)
            return false
        }
    }

    @JvmStatic
    fun setMaterial(
        b: Block,
        mat: Material?,
        physics: Boolean,
        undo: UndoElement,
        currentPlayer: CommandSender
    ): Boolean {
        if (AsyncManager.countEdits) {
            ++AsyncManager.doneOperations
        }
        if (PlayerManager.getPlayerWrapper(currentPlayer).outsideEditRegion(b.x.toLong(), b.y.toLong(), b.z.toLong())) {
            return false
        }
        if (!PlayerManager.getPlayerWrapper(currentPlayer).shouldEdit(b.type)) {
            return false
        }
        try {
            val bs = b.state
            b.setType(mat!!, physics)
            if (undo.currentState != UndoMode.PERFORMING_UNDO && undo.currentState != UndoMode.PERFORMING_REDO)
                undo.addBlock(bs, b.state)
            updateLeafData(mat, b)
            return true
        } catch (e: Exception) {
            invalidMaterial(mat, e, currentPlayer)
            return false
        }
    }

    @JvmStatic
    fun setMaterial(
        b: Block,
        mat: Material?,
        data: BlockData?,
        undo: UndoElement,
        currentPlayer: CommandSender
    ): Boolean {
        if (AsyncManager.countEdits) {
            ++AsyncManager.doneOperations
        }
        if (PlayerManager.getPlayerWrapper(currentPlayer).outsideEditRegion(b.x.toLong(), b.y.toLong(), b.z.toLong())) {
            return false
        }
        if (!PlayerManager.getPlayerWrapper(currentPlayer).shouldEdit(b.type)) {
            return false
        }
        try {
            val bs = b.state
            b.setType(mat!!, false)
            if (undo.currentState != UndoMode.PERFORMING_UNDO && undo.currentState != UndoMode.PERFORMING_REDO)
                undo.addBlock(bs, b.state)
            updateLeafData(mat, b)
            try {
                if (data != null)
                    b.setBlockData(data, false)
            } catch (ignored: Exception) {
            };
            return true
        } catch (e: Exception) {
            invalidMaterial(mat, e, currentPlayer)
            return false
        }
    }

    @JvmStatic
    fun setMaterial(
        b: Block,
        mat: Material?,
        data: BlockData?,
        physics: Boolean,
        undo: UndoElement,
        currentPlayer: CommandSender
    ): Boolean {
        if (AsyncManager.countEdits) {
            ++AsyncManager.doneOperations
        }
        if (PlayerManager.getPlayerWrapper(currentPlayer).outsideEditRegion(b.x.toLong(), b.y.toLong(), b.z.toLong())) {
            return false
        }
        if (!PlayerManager.getPlayerWrapper(currentPlayer).shouldEdit(b.type)) {
            return false
        }
        try {
            val bs = b.state
            b.setType(mat!!, physics)
            if (undo.currentState != UndoMode.PERFORMING_UNDO && undo.currentState != UndoMode.PERFORMING_REDO)
                undo.addBlock(bs, b.state)
            updateLeafData(mat, b)
            try {
                if (data != null)
                    b.setBlockData(data, physics)
            } catch (ignored: Exception) {
            };
            return true
        } catch (e: Exception) {
            invalidMaterial(mat, e, currentPlayer)
            return false
        }
    }

    private fun updateLeafData(mat: Material, b: Block) {
        if (mat.toString().toLowerCase().contains("leaves")) {
            val leafData = b.blockData as Leaves
            leafData.isPersistent = true
            b.blockData = leafData
        }
    }

    @JvmStatic
    fun setMaterial(b: BlockState, mat: Material?, undo: UndoElement, currentPlayer: CommandSender): Boolean {
        if (AsyncManager.countEdits) {
            ++AsyncManager.doneOperations
        }
        if (PlayerManager.getPlayerWrapper(currentPlayer).outsideEditRegion(b.x.toLong(), b.y.toLong(), b.z.toLong())) {
            return false
        }
        if (!PlayerManager.getPlayerWrapper(currentPlayer).shouldEdit(b.type)) {
            return false
        }
        try {
            val bs = b.block.state
            b.type = mat!!
            if (undo.currentState != UndoMode.PERFORMING_UNDO && undo.currentState != UndoMode.PERFORMING_REDO)
                undo.addBlock(bs, b)
            updateLeafDataState(mat, b)
            return true
        } catch (e: Exception) {
            invalidMaterial(mat, e, currentPlayer)
            return false
        }
    }

    @JvmStatic
    fun setMaterial(
        b: BlockState,
        mat: Material?,
        data: BlockData?,
        undo: UndoElement,
        currentPlayer: CommandSender
    ): Boolean {
        if (AsyncManager.countEdits) {
            ++AsyncManager.doneOperations
        }
        if (PlayerManager.getPlayerWrapper(currentPlayer).outsideEditRegion(b.x.toLong(), b.y.toLong(), b.z.toLong())) {
            return false
        }
        if (!PlayerManager.getPlayerWrapper(currentPlayer).shouldEdit(b.type)) {
            return false
        }
        try {
            val bs = b.block.state
            b.type = mat!!
            if (undo.currentState != UndoMode.PERFORMING_UNDO && undo.currentState != UndoMode.PERFORMING_REDO)
                undo.addBlock(bs, b)
            updateLeafDataState(mat, b)
            try {
                if (data != null)
                    b.blockData = data
            } catch (ignored: Exception) {
            };
            return true
        } catch (e: Exception) {
            invalidMaterial(mat, e, currentPlayer)
            return false
        }
    }

    private fun updateLeafDataState(mat: Material, b: BlockState) {
        if (mat.toString().toLowerCase().contains("leaves")) {
            val leafData = b.blockData as Leaves
            leafData.isPersistent = true
            b.blockData = leafData
        }
    }

    private fun invalidMaterial(mat: Material?, e: Exception, currentPlayer: CommandSender) {
        AsyncManager.dropAsync()
        Main.logError(
            "Invalid block ID " + mat?.toString() + " provided. The async queue has been dropped.",
            currentPlayer, e
        )
    }
}