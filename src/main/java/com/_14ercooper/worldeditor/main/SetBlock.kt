package com._14ercooper.worldeditor.main

import com._14ercooper.worldeditor.async.AsyncManager
import com._14ercooper.worldeditor.operations.Operator
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.block.data.type.Leaves

object SetBlock {
    @JvmStatic
    fun setMaterial(b: Block, mat: Material?) {
        if (GlobalVars.countEdits) {
            ++AsyncManager.doneOperations
        }
        if (Main.inEditRegion(b.x.toLong(), b.y.toLong(), b.z.toLong())) {
            return
        }
        try {
            if (GlobalVars.currentUndo != null) GlobalVars.currentUndo.storeBlock(b)
            b.setType(mat!!, false)
            updateLeafData(mat, b)
        } catch (e: Exception) {
            invalidMaterial(mat, e)
        }
    }

    @JvmStatic
    fun setMaterial(b: Block, mat: Material?, physics: Boolean) {
        if (GlobalVars.countEdits) {
            ++AsyncManager.doneOperations
        }
        if (Main.inEditRegion(b.x.toLong(), b.y.toLong(), b.z.toLong())) {
            return
        }
        try {
            if (GlobalVars.currentUndo != null) GlobalVars.currentUndo.storeBlock(b)
            b.setType(mat!!, physics)
            updateLeafData(mat, b)
        } catch (e: Exception) {
            invalidMaterial(mat, e)
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
    fun setMaterial(b: BlockState, mat: Material?) {
        if (GlobalVars.countEdits) {
            ++AsyncManager.doneOperations
        }
        if (Main.inEditRegion(b.x.toLong(), b.y.toLong(), b.z.toLong())) {
            return
        }
        try {
            if (GlobalVars.currentUndo != null) GlobalVars.currentUndo.storeBlock(b.block)
            b.type = mat!!
            updateLeafDataState(mat, b)
        } catch (e: Exception) {
            invalidMaterial(mat, e)
        }
    }

    private fun updateLeafDataState(mat: Material, b: BlockState) {
        if (mat.toString().toLowerCase().contains("leaves")) {
            val leafData = b.blockData as Leaves
            leafData.isPersistent = true
            b.blockData = leafData
        }
    }

    private fun invalidMaterial(mat: Material?, e: Exception) {
        GlobalVars.asyncManager.dropAsync()
        Main.logError(
            "Invalid block ID " + mat?.toString() + " provided. The async queue has been dropped.",
            Operator.currentPlayer, e
        )
    }
}