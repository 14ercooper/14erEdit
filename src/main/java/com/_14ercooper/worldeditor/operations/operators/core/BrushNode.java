package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.brush.shapes.Multi;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BrushNode extends Node {

    public BrushShape shape = null;
    public Node op = null;

    @Override
    public BrushNode newNode(CommandSender currentPlayer) {
        try {
            BrushNode node = new BrushNode();
            node.shape = Brush.GetBrushShape(GlobalVars.operationParser.parseStringNode(currentPlayer).contents);
            do {
                String nextText = GlobalVars.operationParser.parseStringNode(currentPlayer).getText();
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
                node.op = GlobalVars.operationParser.parsePart(currentPlayer);

            return node;
        } catch (Exception e) {
            Main.logError("Could not create brush node. Did you provide the correct number of arguments?",
                    currentPlayer, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state) {
        int x = state.getCurrentBlock().getX();
        int y = state.getCurrentBlock().getY();
        int z = state.getCurrentBlock().getZ();
        if (!(shape instanceof Multi)) {
            // Build an array of all blocks to operate on
            BlockIterator blockArray = shape.GetBlocks(x, y, z, state.getCurrentBlock().getWorld(), state.getCurrentPlayer());

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
            List<BlockIterator> iters = multiShape.getIters(x, y, z, state.getCurrentBlock().getWorld(), state.getCurrentPlayer());
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
