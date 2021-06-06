package com._14ercooper.worldeditor.operations

import com._14ercooper.worldeditor.async.AsyncManager
import com._14ercooper.worldeditor.main.GlobalVars
import com._14ercooper.worldeditor.main.Main.Companion.logError
import com._14ercooper.worldeditor.operations.operators.core.EntryNode
import com._14ercooper.worldeditor.operations.type.*
import com._14ercooper.worldeditor.undo.UndoElement
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Operator {
    private val entryNode: EntryNode?
    fun operateOnBlock(block: Block?, p: CommandSender?, undo : UndoElement) {
        try {
            // Set global operator variables
            currentOperator = this
            currentBlock = block
            currentUndo = undo
            currentPlayer = p
                ?: if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                    Bukkit.getOnlinePlayers().toTypedArray()[0]
                } else {
                    GlobalVars.plugin.server.consoleSender
                }
            currentWorld = if (p != null && p is Player) {
                p.world
            }
            else {
                GlobalVars.plugin.server.worlds[0]
            }

            // Perform the operation
            entryNode!!.performNode()
        } catch (e: Exception) {
            logError("Could not perform operation. Please check your syntax.", p, e)
            AsyncManager.dropAsync()
        }
    }

    fun operateOnBlock(block: Block?, undo : UndoElement) {
        try {
            // Set global operator variables
            currentOperator = this
            currentBlock = block
            currentUndo = undo
            currentPlayer = (if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                Bukkit.getOnlinePlayers().toTypedArray()[0]
            } else {
                null
            }) ?: GlobalVars.plugin.server.consoleSender
            val p = if (Bukkit.getOnlinePlayers().isNotEmpty()) Bukkit.getOnlinePlayers().toTypedArray()[0] else null
            currentWorld = p?.world ?: GlobalVars.plugin.server.worlds[0]

            // Perform the operation
            entryNode!!.performNode()
        } catch (e: Exception) {
            logError("Could not perform operation. Please check your syntax.", Bukkit.getConsoleSender(), e)
            AsyncManager.dropAsync()
        }
    }

    fun messyOperate(): Boolean {
        return try {
            entryNode!!.performNode()
        } catch (e: Exception) {
            logError("Could not perform operation. Please check your syntax.", Bukkit.getConsoleSender(), e)
            AsyncManager.dropAsync()
            false
        }
    }

    constructor(op: String, p: CommandSender?) {
        currentPlayer = p ?: GlobalVars.plugin.server.consoleSender
        currentWorld = if (p != null && p is Player) {
            p.world
        }
        else {
            GlobalVars.plugin.server.worlds[0]
        }
        entryNode = GlobalVars.operationParser.parseOperation(op)
    }

    constructor(e: EntryNode, p: CommandSender?) {
        currentPlayer = p ?: GlobalVars.plugin.server.consoleSender
        currentWorld = if (p != null && p is Player) {
            p.world
        }
        else {
            GlobalVars.plugin.server.worlds[0]
        }
        entryNode = e
    }

    companion object {
        var currentOperator: Operator? = null
        @JvmField
        var currentBlock: Block? = null
        @JvmField
        var currentPlayer: CommandSender = GlobalVars.plugin.server.consoleSender
        @JvmField
        var currentWorld: World = GlobalVars.plugin.server.worlds[0]
//        private var firstPlayer: Player? = null
        @JvmField
        var ignoringPhysics = false // False to ignore physics, true to perform physics 'cause Minecraft
        @JvmField
        var inSetNode = false
        @JvmField
        var currentUndo : UndoElement? = null;

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