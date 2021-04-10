package com._14ercooper.worldeditor.async

import com._14ercooper.schematics.SchemLite
import com._14ercooper.worldeditor.blockiterator.BlockIterator
import com._14ercooper.worldeditor.operations.Operator
import com._14ercooper.worldeditor.undo.Undo
import org.bukkit.entity.Player

class AsyncOperation {
    val key: String
    var operation: Operator? = null
    var blocks: BlockIterator? = null
    var player: Player? = null
    var undo: Undo? = null
    var undoRunning = false

    constructor(o: Operator?, p: Player?, b: BlockIterator?) {
        key = "iteredit"
        operation = o
        player = p
        blocks = b
    }

    constructor(o: Operator?, b: BlockIterator?) {
        key = "rawiteredit"
        operation = o
        blocks = b
    }

    // New schematics system
    var schem: SchemLite? = null
    private var origin = intArrayOf()

    constructor(sl: SchemLite?, saveSchem: Boolean, o: IntArray, p: Player) {
        schem = sl
        origin = o
        blocks = schem!!.getIterator(origin[0], origin[1], origin[2], p.world)
        key = if (saveSchem) {
            "saveschem"
        } else {
            "loadschem"
        }
        player = p
    }

    // Selection move/stack
    var offset = intArrayOf()
    var times = 0
    var delOriginal = false

    // Uses the same iterator as other functions
    constructor(
        selectionIter: BlockIterator?, cloneOffset: IntArray, cloneTimes: Int, delOriginalBlocks: Boolean,
        p: Player?
    ) {
        key = "selclone"
        blocks = selectionIter
        offset = cloneOffset
        times = cloneTimes
        delOriginal = delOriginalBlocks
        player = p
    }

    // Multibrush
    lateinit var iterators: MutableList<BlockIterator>
    lateinit var operations: MutableList<Operator>

    constructor(iterators: List<BlockIterator>, operations: List<Operator>, p: Player?) {
        key = "multibrush"
        this.iterators = iterators as MutableList<BlockIterator>
        this.operations = operations as MutableList<Operator>
        player = p
    }
}