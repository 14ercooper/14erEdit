// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrushNode extends Node {

    public BrushShape shape = null;
    public final List<EntryNode> entry = new ArrayList<>();

    @Override
    public BrushNode newNode(ParserState parserState) {
        try {
            BrushNode node = new BrushNode();
            node.shape = Brush.GetBrushShape(Parser.parseStringNode(parserState).contents);
            do {
                StringNode stringNode = Parser.parseStringNode(parserState);
                if (stringNode == null) {
                    break;
                }
                String nextText = stringNode.getText();
                if (nextText.equalsIgnoreCase("end")) {
                    parserState.setIndex(parserState.getIndex() + 1);
                    break;
                }
                node.shape.addNewArgument(nextText);
            }
            while (node.shape.lastInputProcessed());

            parserState.setIndex(parserState.getIndex() - 1);
            if (node.shape.gotEnoughArgs()) {
                throw new Exception("Too few arguments for the brush shape");
            }

            for (int i = 0; i < node.shape.operatorCount(); i++) {
                Node op = Parser.parsePart(parserState);
                node.entry.add(new EntryNode(op, parserState.getIndex() + 1));
            }

            return node;
        } catch (Exception e) {
            Main.logError("Could not create brush node. Did you provide the correct number of arguments?",
                    parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        int x = state.getCurrentBlock().x;
        int y = state.getCurrentBlock().y;
        int z = state.getCurrentBlock().z;

        List<Operator> operations = new ArrayList<>();
        for (EntryNode e : entry) {
            operations.add(new Operator(e));
        }
        shape.runBrush(operations, x, y, z, (Player) state.getCurrentPlayer());
        return true;
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
