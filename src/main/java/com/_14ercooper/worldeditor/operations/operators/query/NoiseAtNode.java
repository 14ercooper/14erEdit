package com._14ercooper.worldeditor.operations.operators.query;

import org.bukkit.block.Block;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.function.NoiseNode;

public class NoiseAtNode extends Node {

    NoiseNode noise;
    NumberNode midplane, amplitude;
    Node function;

    @Override
    public NoiseAtNode newNode() {
	NoiseAtNode node = new NoiseAtNode();
	node.noise = (NoiseNode) GlobalVars.operationParser.parsePart();
	node.midplane = GlobalVars.operationParser.parseNumberNode();
	node.amplitude = GlobalVars.operationParser.parseNumberNode();
	node.function = GlobalVars.operationParser.parsePart();
	return node;
    }

    @Override
    public boolean performNode() {
	Block b = Operator.currentBlock;
	int x = b.getX();
	int z = b.getZ();
	int y = 64;
	if (midplane.getValue() < 0) {
	    y = (int) ((noise.getNum() * amplitude.getValue()) + b.getY());
	}
	else {
	    y = (int) ((noise.getNum() * amplitude.getValue()) + midplane.getValue());
	}
	Operator.currentBlock = Operator.currentPlayer.getWorld().getBlockAt(x, y, z);
	boolean result = function.performNode();
	Operator.currentBlock = b;
	return result;
    }

    @Override
    public int getArgCount() {
	return 3;
    }

}
