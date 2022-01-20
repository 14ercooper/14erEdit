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

package com._14ercooper.worldeditor.async

import com._14ercooper.schematics.SchemLite
import com._14ercooper.worldeditor.blockiterator.BlockIterator
import com._14ercooper.worldeditor.operations.Operator
import com._14ercooper.worldeditor.undo.UndoElement
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AsyncOperation {
    val key: String
    var operation: Operator? = null
    var blocks: BlockIterator? = null
    var player: CommandSender
    var undo: UndoElement

    constructor(o: Operator?, p: CommandSender, b: BlockIterator?, thisUndo: UndoElement) {
        key = "iteredit"
        operation = o
        player = p
        blocks = b
        undo = thisUndo
    }

    // New undo system
    var undoList: MutableList<UndoElement>? = null

    constructor(undos: MutableList<UndoElement>, thisPlayer: CommandSender) {
        key = "undoedit"
        player = thisPlayer
        undoList = undos
        undo = undos[0]
    }

    // New schematics system
    var schem: SchemLite? = null
    private var origin = intArrayOf()
    var startedWrite: Boolean = false

    constructor(sl: SchemLite?, saveSchem: Boolean, o: IntArray, p: CommandSender, thisUndo: UndoElement) {
        schem = sl
        origin = o
        blocks = schem!!.getIterator(
            origin[0], origin[1], origin[2], if (p is Player) {
                p.world
            } else {
                Bukkit.getServer().worlds[0]
            }
        )
        key = if (saveSchem) {
            "saveschem"
        } else {
            "loadschem"
        }
        player = p
        undo = thisUndo
    }

    // Selection move/stack
    var offset = intArrayOf()
    var times = 0
    var delOriginal = false

    // Uses the same iterator as other functions
    constructor(
        selectionIter: BlockIterator?, cloneOffset: IntArray, cloneTimes: Int, delOriginalBlocks: Boolean,
        p: CommandSender, thisUndo: UndoElement
    ) {
        key = "selclone"
        blocks = selectionIter
        offset = cloneOffset
        times = cloneTimes
        delOriginal = delOriginalBlocks
        player = p
        undo = thisUndo
    }

    // Multibrush
    lateinit var iterators: MutableList<BlockIterator>
    lateinit var operations: MutableList<Operator>

    constructor(iterators: List<BlockIterator>, operations: List<Operator>, p: CommandSender, thisUndo: UndoElement) {
        key = "multibrush"
        this.iterators = iterators as MutableList<BlockIterator>
        this.operations = operations as MutableList<Operator>
        player = p
        undo = thisUndo
    }
}