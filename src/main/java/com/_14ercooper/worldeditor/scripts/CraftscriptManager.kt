// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.scripts

import com._14ercooper.worldeditor.main.Main.Companion.logDebug
import com._14ercooper.worldeditor.main.Main.Companion.logError
import com._14ercooper.worldeditor.undo.UndoElement
import org.bukkit.command.CommandSender
import java.util.*

object CraftscriptManager  // Create a new manager
{
    // Undo
    private var currentPlayer: CommandSender? = null
    private var undoElement: UndoElement? = null
    private var usedUndo = false

    // Stores registered scripts
    @JvmField
    val registeredScripts: MutableMap<String, Craftscript> = HashMap()

    // Register a new Craftscript, called by label label and handled by handler
    fun registerCraftscript(label: String, handler: Craftscript) {
        registeredScripts[label] = handler
    }

    // Run the Craftscript label, with arguments args, and player player
    fun runCraftscript(label: String, args: LinkedList<String?>?, player: CommandSender?): Boolean {
        logDebug("Calling Craftsript: $label")
        try {
            usedUndo = false
            currentPlayer = player
            registeredScripts[label]!!.perform(args, player, label)
            if (usedUndo) {
                undoElement!!.userUndo.finalizeUndo(undoElement!!)
            }
        } catch (e: Exception) {
            logError("Error performing CraftScript. Check your syntax?", player, e)
        }
        return true
    }
}