/**
 * This file is part of 14erEdit.
 *
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

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