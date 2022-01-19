// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


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