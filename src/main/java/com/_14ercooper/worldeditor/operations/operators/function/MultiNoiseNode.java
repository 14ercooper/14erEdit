package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.DummyState;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

import java.util.ArrayList;
import java.util.List;

public class MultiNoiseNode extends Node {

    NumberNode noiseCount;
    NoiseNode noiseFunction;
    final List<Node> noises = new ArrayList<>();

    @Override
    public MultiNoiseNode newNode(ParserState parserState) {
        try {
            MultiNoiseNode node = new MultiNoiseNode();

            try {
                node.noiseCount = Parser.parseNumberNode(parserState);
            } catch (Exception e) {
                Main.logError("Multinoise node expected first argument to be a number, but it was not.",
                        parserState, e);
                return null;
            }

            try {
                node.noiseFunction = (NoiseNode) Parser.parsePart(parserState);
            } catch (Exception e) {
                Main.logError("Multinoise node requires a noise node, but none was found.", parserState, e);
                return null;
            }

            try {
                for (int i = 0; i < node.noiseCount.getMaxInt(new DummyState(parserState.getCurrentPlayer())); i++) {
                    node.noises.add(Parser.parsePart(parserState));
                }
            } catch (Exception e) {
                Main.logError("Error parsing nodes for multinoise, likely ran out of nodes.", parserState, e);
                return null;
            }

            return node;
        } catch (Exception e) {
            Main.logError("Could not create multinoise node. Are you missing an argument?", parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        // Get the noise value and the value of the noise count
        int c = noiseCount.getInt(state) + 1;
        float n = NoiseNode.scaleTo255(noiseFunction.getNum(state));
        n = n * sigmoid(n / 127.5f);

        // Collapse to [0,c-1] range
        float cInv = (float) c / 255.0f;
        float nColl = n * cInv;
        int nodeToRun = (int) (nColl);

        if (nodeToRun >= noises.size()) {
            nodeToRun = noises.size() - 1;
        }

        // Perform correct node
        return noises.get(nodeToRun).performNode(state, true);
    }

    private float sigmoid(float val) {
        return (float) (1.0f / (1 + Math.exp(-2.0f * (val - 1.0f))));
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
