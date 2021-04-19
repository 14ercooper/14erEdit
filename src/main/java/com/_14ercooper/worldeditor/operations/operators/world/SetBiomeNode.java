package com._14ercooper.worldeditor.operations.operators.world;

import org.bukkit.block.Biome;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.StringNode;

public class SetBiomeNode extends Node {

    StringNode biome;

    @Override
    public Node newNode() {
        try {
            SetBiomeNode node = new SetBiomeNode();
            node.biome = GlobalVars.operationParser.parseStringNode();
            if (node.biome == null) {
                Main.logError("Could not parse set biome node. Did you provide a biome?", Operator.currentPlayer, null);
            }
            return node;
        } catch (Exception e) {
            Main.logError("Error parisng biome node. Please check your syntax.", Operator.currentPlayer, e);
            return null;
        }
    }

    @Override
    public boolean performNode() {
        try {
            Operator.currentWorld.setBiome(Operator.currentBlock.getX(), Operator.currentBlock.getY(),
                    Operator.currentBlock.getZ(), Biome.valueOf(biome.getText()));
            return true;
        } catch (Exception e) {
            Main.logError("Could not perform set biome node. Did you provide a valid biome?", Operator.currentPlayer, e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
