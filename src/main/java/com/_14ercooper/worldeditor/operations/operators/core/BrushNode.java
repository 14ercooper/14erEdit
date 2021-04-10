package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.brush.shapes.Multi;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

import java.util.List;

public class BrushNode extends Node {

    public BrushShape shape = null;
    public Node op = null;

    @Override
    public BrushNode newNode() {
        try {
            BrushNode node = new BrushNode();
            node.shape = Brush.GetBrushShape(GlobalVars.operationParser.parseStringNode().contents);
            do {
                String nextText = GlobalVars.operationParser.parseStringNode().getText();
                if (nextText.equalsIgnoreCase("end")) {
                    GlobalVars.operationParser.index++;
                    break;
                }
                node.shape.addNewArgument(nextText);
            }
            while (node.shape.lastInputProcessed());
            GlobalVars.operationParser.index--;
            if (node.shape.gotEnoughArgs()) {
                throw new Exception();
            }

            if (!(node.shape instanceof Multi))
                node.op = GlobalVars.operationParser.parsePart();

            Operator.currentPlayer.sendMessage(
                    "§aNOTE: Nested brushes run in large edit mode, so no undo will be registered. Please be careful.");
            return node;
        } catch (Exception e) {
            Main.logError("Could not create brush node. Did you provide the correct number of arguments?",
                    Operator.currentPlayer, e);
            return null;
        }
    }

    @Override
    public boolean performNode() {
        int x = Operator.currentBlock.getX();
        int y = Operator.currentBlock.getY();
        int z = Operator.currentBlock.getZ();
        if (!(shape instanceof Multi)) {
            // Build an array of all blocks to operate on
            BlockIterator blockArray = shape.GetBlocks(x, y, z, Operator.currentBlock.getWorld());

            if (blockArray.getTotalBlocks() == 0) {
                return false;
            }
            EntryNode entry = new EntryNode(op);
            Operator operation = new Operator(entry, Operator.currentPlayer);

            Main.logDebug("Block array size is " + blockArray.getTotalBlocks()); // -----

            GlobalVars.asyncManager.scheduleEdit(operation, null, blockArray, true);

        } else {
            // It's a multi-operator
            Multi multiShape = (Multi) shape;
            List<BlockIterator> iters = multiShape.getIters(x, y, z, Operator.currentBlock.getWorld());
            List<Operator> ops = multiShape.getOps(x, y, z);

            GlobalVars.asyncManager.scheduleEdit(iters, ops, Operator.currentPlayer);

        }
        return true;
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
