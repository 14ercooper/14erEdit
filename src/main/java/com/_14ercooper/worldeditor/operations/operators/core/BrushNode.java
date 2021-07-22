package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.brush.shapes.Multi;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

import java.util.List;

public class BrushNode extends Node {

    public BrushShape shape = null;
    public Node op = null;

    @Override
    public BrushNode newNode(ParserState parserState) {
        try {
            BrushNode node = new BrushNode();
            node.shape = Brush.GetBrushShape(Parser.parseStringNode(parserState).contents);
            do {
                String nextText = Parser.parseStringNode(parserState).getText();
                if (nextText.equalsIgnoreCase("end")) {
                    parserState.setIndex(parserState.getIndex() + 1);
                    break;
                }
                node.shape.addNewArgument(nextText);
            }
            while (node.shape.lastInputProcessed());
            parserState.setIndex(parserState.getIndex() - 1);
            if (node.shape.gotEnoughArgs()) {
                throw new Exception();
            }

            if (!(node.shape instanceof Multi))
                node.op = Parser.parsePart(parserState);

            return node;
        } catch (Exception e) {
            Main.logError("Could not create brush node. Did you provide the correct number of arguments?",
                    parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state) {
        int x = state.getCurrentBlock().x;
        int y = state.getCurrentBlock().y;
        int z = state.getCurrentBlock().z;
        if (!(shape instanceof Multi)) {
            // Build an array of all blocks to operate on
            BlockIterator blockArray = shape.GetBlocks(x, y, z, state.getCurrentBlock().block.getWorld(), state.getCurrentPlayer());

            if (blockArray.getTotalBlocks() == 0) {
                return false;
            }
            EntryNode entry = new EntryNode(op);
            Operator operation = new Operator(entry);

            Main.logDebug("Block array size is " + blockArray.getTotalBlocks()); // -----

            AsyncManager.scheduleEdit(operation, state.getCurrentPlayer(), blockArray, state.getCurrentUndo());

        } else {
            // It's a multi-operator
            Multi multiShape = (Multi) shape;
            List<BlockIterator> iters = multiShape.getIters(x, y, z, state.getCurrentBlock().block.getWorld(), state.getCurrentPlayer());
            List<Operator> ops = multiShape.getOps(x, y, z);

            AsyncManager.scheduleEdit(iters, ops, state.getCurrentPlayer(), state.getCurrentUndo());

        }
        return true;
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
