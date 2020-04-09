package com.fourteener.worldeditor.operations.operators.core;

import org.bukkit.Material;

import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class StringNode extends Node {

	public String contents;
	
	@Override
	public StringNode newNode() {
		
		return new StringNode();
	}

	@Override
	public boolean performNode() {
		return Operator.currentBlock.getType() == Material.matchMaterial(contents);
	}
	
	public String getText () {
		return contents;
	}

	@Override
	public int getArgCount() {
		return 1;
	}

}
