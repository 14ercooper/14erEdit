package com._14ercooper.worldeditor.operations

import com._14ercooper.worldeditor.main.GlobalVars
import com._14ercooper.worldeditor.main.Main.Companion.logError
import com._14ercooper.worldeditor.operations.operators.core.EntryNode
import com._14ercooper.worldeditor.operations.type.*
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.entity.Player

class Operator {
    private val entryNode: EntryNode?
    fun operateOnBlock(block: Block?, p: Player?) {
        try {
            // Set global operator variables
            currentOperator = this
            currentBlock = block
            currentPlayer = p
            if (currentPlayer == null) {
                if (firstPlayer == null) {
                    firstPlayer = Bukkit.getOnlinePlayers().toTypedArray()[0] as Player
                }
                currentPlayer = firstPlayer
            }

            // Perform the operation
            entryNode!!.performNode()
        } catch (e: Exception) {
            logError("Could not perform operation. Please check your syntax.", p, e)
            GlobalVars.asyncManager.dropAsync()
        }
    }

    fun operateOnBlock(block: Block?) {
        try {
            // Set global operator variables
            currentOperator = this
            currentBlock = block
            currentPlayer = null
            if (firstPlayer == null) {
                firstPlayer = Bukkit.getOnlinePlayers().toTypedArray()[0] as Player
            }
            currentPlayer = firstPlayer

            // Perform the operation
            entryNode!!.performNode()
        } catch (e: Exception) {
            logError("Could not perform operation. Please check your syntax.", Bukkit.getConsoleSender(), e)
            GlobalVars.asyncManager.dropAsync()
        }
    }

    fun messyOperate(): Boolean {
        return try {
            entryNode!!.performNode()
        } catch (e: Exception) {
            logError("Could not perform operation. Please check your syntax.", Bukkit.getConsoleSender(), e)
            GlobalVars.asyncManager.dropAsync()
            false
        }
    }

    constructor(op: String, p: Player?) {
        currentPlayer = p // Used to show errors in the parse process
        entryNode = GlobalVars.operationParser.parseOperation(op)
    }

    constructor(e: EntryNode, p: Player?) {
        currentPlayer = p
        entryNode = e
    }

    companion object {
        var currentOperator: Operator? = null
        @JvmField
        var currentBlock: Block? = null
        @JvmField
        var currentPlayer: Player? = null
        private var firstPlayer: Player? = null
        @JvmField
        var ignoringPhysics = false // False to ignore physics, true to perform physics 'cause Minecraft
        @JvmField
        var inSetNode = false

        // is screwy
        @JvmField
        val blockVars: Map<String, BlockVar> = HashMap()
        @JvmField
        val itemVars: Map<String, ItemVar> = HashMap()
        @JvmField
        val numericVars: Map<String, NumericVar> = HashMap()
        @JvmField
        val monsterVars: Map<String, MonsterVar> = HashMap()
        @JvmField
        val spawnerVars: Map<String, SpawnerVar> = HashMap()
        @JvmField
        val fileLoads: Map<String, Operator> = HashMap()
    }
}