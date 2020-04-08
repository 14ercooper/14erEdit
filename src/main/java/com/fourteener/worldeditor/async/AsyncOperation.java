package com.fourteener.worldeditor.async;

import java.util.ArrayDeque;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.selection.Clipboard;

public class AsyncOperation {
	protected String key = "";
	protected Operator operation = null;
	protected ArrayDeque<Block> toOperate = null;
	protected Player player = null;
	protected Clipboard clipboard = null; 
	
	public AsyncOperation (boolean startUndo, Player p) {
		if (startUndo) {
			key = "startUndo";
			player = p;
		}
		else {
			key = "finishUndo";
			player = p;
		}
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
