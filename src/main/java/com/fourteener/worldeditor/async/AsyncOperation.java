package com.fourteener.worldeditor.async;

import java.util.ArrayDeque;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.selection.Clipboard;
import com.fourteener.worldeditor.undo.Undo;

public class AsyncOperation {
	protected String key = "";
	protected Operator operation = null;
	protected ArrayDeque<Block> toOperate = null;
	protected BlockIterator blocks = null;
	protected Player player = null;
	protected Clipboard clipboard = null;
	protected String path = "";
	protected Undo undo = null;
	protected boolean undoRunning = false;
	
	public AsyncOperation (Operator o, Player p, BlockIterator b) {
		key = "iteredit";
		operation = o;
		player = p;
		blocks = b;
	}
	
	public AsyncOperation (Operator o, BlockIterator b) {
		key = "rawiteredit";
		operation = o;
		blocks = b;
	}
	
	public AsyncOperation (BlockIterator b, Player p, String filepath) {
		key = "saveschem";
		player = p;
		blocks = b;
		path = filepath;
	}
	
	public AsyncOperation (Player p, String filepath) {
		key = "pasteschem";
		player = p;
		path = filepath;
	}
	
	public AsyncOperation (Operator o, Player p, ArrayDeque<Block> b) {
		key = "edit";
		operation = o;
		player = p;
		toOperate = b;
	}
	
	public AsyncOperation (Clipboard c, ArrayDeque<Block> b, boolean save) {
		if (save) {
			key = "saveclip";
		}
		else {
			key = "loadclip";
		}
		clipboard = c;
		toOperate = b;
	}
	
	public AsyncOperation (Operator o, ArrayDeque<Block> b) {
		key = "rawedit";
		operation = o;
		toOperate = b;
	}
	
	public AsyncOperation (Operator o) {
		key = "messyedit";
		operation = o;
	}
}
