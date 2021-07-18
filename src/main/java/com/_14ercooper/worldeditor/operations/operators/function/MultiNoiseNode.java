package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.DummyState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MultiNoiseNode extends Node {

    NumberNode noiseCount;
    NoiseNode noiseFunction;
    final List<Node> noises = new ArrayList<>();

    @Override
    public MultiNoiseNode newNode(CommandSender currentPlayer) {
        try {
            MultiNoiseNode node = new MultiNoiseNode();

            try {
                node.noiseCount = GlobalVars.operationParser.parseNumberNode(currentPlayer);
            } catch (Exception e) {
                Main.logError("Multinoise node expected first argument to be a number, but it was not.",
                        currentPlayer, e);
                return null;
            }

            try {
                node.noiseFunction = (NoiseNode) GlobalVars.operationParser.parsePart(currentPlayer);
            } catch (Exception e) {
                Main.logError("Multinoise node requires a noise node, but none was found.", currentPlayer, e);
                return null;
            }

            try {
                for (int i = 0; i < node.noiseCount.getMaxInt(new DummyState(currentPlayer)); i++) {
                    node.noises.add(GlobalVars.operationParser.parsePart(currentPlayer));
                }
            } catch (Exception e) {
                Main.logError("Error parsing nodes for multinoise, likely ran out of nodes.", currentPlayer, e);
                return null;
            }

            return node;
        } catch (Exception e) {
            Main.logError("Could not create multinoise node. Are you missing an argument?", currentPlayer, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state) {
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
        return noises.get(nodeToRun).performNode(state);
    }

    private float sigmoid(float val) {
        return (float) (1.0f / (1 + Math.exp(-2.0f * (val - 1.0f))));
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
