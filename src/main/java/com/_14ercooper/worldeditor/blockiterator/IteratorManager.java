package com._14ercooper.worldeditor.blockiterator;

import java.util.HashMap;
import java.util.Map;

public class IteratorManager {

    final Map<String, BlockIterator> iterators = new HashMap<>();

    // Register a new block iterator
    public void addIterator(String key, BlockIterator iterator) {
        iterators.put(key, iterator);
    }

    // Get the referenced block iterator
    public BlockIterator getIterator(String key) {
        return iterators.get(key);
    }
}
