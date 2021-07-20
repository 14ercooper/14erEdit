package com._14ercooper.worldeditor.blockiterator

object IteratorManager {
    val iterators: MutableMap<String, BlockIterator> = HashMap()

    // Register a new block iterator
    fun addIterator(key: String, iterator: BlockIterator) {
        iterators[key] = iterator
    }

    // Get the referenced block iterator
    fun getIterator(key: String): BlockIterator? {
        return iterators[key]
    }
}