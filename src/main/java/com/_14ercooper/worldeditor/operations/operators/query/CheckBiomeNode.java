package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckBiomeNode extends Node {

    final List<String> biomes = new ArrayList<>();

    @Override
    public Node newNode(CommandSender currentPlayer) {
        try {
            CheckBiomeNode node = new CheckBiomeNode();
            node.biomes.addAll(Arrays.asList(GlobalVars.operationParser.parseStringNode(currentPlayer).getText().split(",")));
            if (node.biomes.size() == 0) {
                Main.logError("Could not parse set biome node. Did you provide a biome?", currentPlayer, null);
            }
            return node;
        } catch (Exception e) {
            Main.logError("Error parisng biome node. Please check your syntax.", currentPlayer, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            if (!(state.getCurrentBlock().getChunk().isLoaded())) {
                state.getCurrentBlock().getChunk().load(true);
            }
            String currBiome = state.getCurrentBlock().getBiome().toString();
            for (String s : biomes) {
                if (currBiome.equalsIgnoreCase(s))
                    return true;
            }
            return false;
        } catch (Exception e) {
            Main.logError("Error perfoming check biome node. Did you provide valid biomes?", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
