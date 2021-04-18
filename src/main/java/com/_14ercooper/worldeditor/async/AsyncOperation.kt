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
    var undo : UndoElement

    constructor(o: Operator?, p: CommandSender, b: BlockIterator?, thisUndo : UndoElement) {
        key = "iteredit"
        operation = o
        player = p
        blocks = b
        undo = thisUndo
    }

    // New undo system
    var undoList : MutableList<UndoElement>? = null
    constructor(undos : MutableList<UndoElement>) {
        key = "undoedit"
        player = Bukkit.getServer().consoleSender
        undoList = undos
        undo = undos[0]
    }

//    constructor(o: Operator?, b: BlockIterator?, thisUndo : UndoElement) {
//        key = "rawiteredit"
//        operation = o
//        blocks = b
//        undo = thisUndo
//    }

    // New schematics system
    var schem: SchemLite? = null
    private var origin = intArrayOf()
    var startedWrite : Boolean = false

    constructor(sl: SchemLite?, saveSchem: Boolean, o: IntArray, p: CommandSender, thisUndo : UndoElement) {
        schem = sl
        origin = o
        blocks = schem!!.getIterator(origin[0], origin[1], origin[2], if (p is Player) { p.world} else {Bukkit.getServer().worlds[0]})
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
        p: CommandSender, thisUndo : UndoElement
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

    constructor(iterators: List<BlockIterator>, operations: List<Operator>, p: CommandSender, thisUndo : UndoElement) {
        key = "multibrush"
        this.iterators = iterators as MutableList<BlockIterator>
        this.operations = operations as MutableList<Operator>
        player = p
        undo = thisUndo
    }
}