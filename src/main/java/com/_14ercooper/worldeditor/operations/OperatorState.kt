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

import com._14ercooper.worldeditor.blockiterator.BlockWrapper
import com._14ercooper.worldeditor.undo.UndoElement
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.command.CommandSender

open class OperatorState(
    var currentBlock: BlockWrapper,
    var currentPlayer: CommandSender,
    var currentWorld: World,
    var currentUndo: UndoElement,
    var originLocation: Location
) {
    var ignoringPhysics = false

    fun setCurrentBlock(b: Block) {
        currentBlock = BlockWrapper(b, b.x, b.y, b.z)
    }

    val otherValues : MutableMap<String, String> = mutableMapOf()

    var variables = mutableMapOf<String,Double>()

    fun setVariable(name: String, value: Double) {
        variables[name] = value
    }

    fun getVariable(name: String): Double {
        return variables.getOrDefault(name, 0.0)
    }

    fun resetVariables() {
        variables = mutableMapOf()
    }
}