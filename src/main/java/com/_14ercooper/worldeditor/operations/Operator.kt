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