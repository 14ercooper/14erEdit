package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.StringNode;
import org.bukkit.block.Biome;

public class SetBiomeNode extends Node {

    StringNode biome;

    @Override
    public Node newNode(ParserState parserState) {
        try {
            SetBiomeNode node = new SetBiomeNode();
            node.biome = Parser.parseStringNode(parserState);
            if (node.biome.getText().isBlank()) {
                Main.logError("Could not parse set biome node. Did you provide a biome?", parserState, null);
            }
            return node;
        } catch (Exception e) {
            Main.logError("Error parisng biome node. Please check your syntax.", parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            if (!(state.getCurrentBlock().getChunk().isLoaded())) {
                state.getCurrentBlock().getChunk().load(true);
            }
            state.getCurrentBlock().getWorld().setBiome(state.getCurrentBlock().getX(), state.getCurrentBlock().getY(), state.getCurrentBlock().getZ(), Biome.valueOf(biome.getText()));
            state.getCurrentBlock().setBiome(Biome.valueOf(biome.getText()));
            return true;
        } catch (Exception e) {
            Main.logError("Could not perform set biome node. Did you provide a valid biome?", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
