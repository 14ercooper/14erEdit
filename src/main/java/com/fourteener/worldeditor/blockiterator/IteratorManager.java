package com.fourteener.worldeditor.blockiterator;

import java.util.HashMap;
import java.util.Map;

public class IteratorManager {
	
	Map<String, BlockIterator> iterators = new HashMap<String, BlockIterator>();
	
	// Register a new block iterator
	public boolean addIterator(String key, BlockIterator iterator) {
		return iterators.put(key, iterator) != null;
	}
	
	// Get the referenced block iterator
	public BlockIterator getIterator(String key) {
		return iterators.get(key);
	}
}
