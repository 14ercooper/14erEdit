package com._14ercooper.worldeditor.operations.operators.core;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class BrushNode extends Node {

	public BrushShape shape = null;
	public List<Double> brushArgs = null;
	public Node op = null;
	
	@Override
	public BrushNode newNode() {
		try {
		BrushNode node = new BrushNode();
		node.shape = Brush.GetBrushShape(GlobalVars.operationParser.parseStringNode().contents);
		node.brushArgs = new ArrayList<Double>();
		for (int i = 0; i < (int) (double) node.shape.GetArgCount(); i++) {
			node.brushArgs.add(GlobalVars.operationParser.parseNumberNode().getValue());
		}
		node.op = GlobalVars.operationParser.parsePart();
		return node;
		} catch (Exception e) {
			Main.logError("Could not create brush node. Did you provide the correct number of arguments?", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public boolean performNode() {
		
		BlockIterator iter = shape.GetBlocks(brushArgs, Operator.currentBlock.getX(), Operator.currentBlock.getY(), Operator.currentBlock.getZ());
		EntryNode entry = new EntryNode(op);
		Operator oper = new Operator(entry, Operator.currentPlayer);
		
		// Eventually need to call this
		GlobalVars.asyncManager.scheduleEdit(oper, Operator.currentPlayer, iter, true);
		return false;
	}

	@Override
	public int getArgCount() {
		return 3;
	}

}
