/**
 * This file is part of 14erEdit.
 *
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckBiomeNode extends Node {

    final List<String> biomes = new ArrayList<>();

    @Override
    public Node newNode(ParserState parserState) {
        try {
            CheckBiomeNode node = new CheckBiomeNode();
            node.biomes.addAll(Arrays.asList(Parser.parseStringNode(parserState).getText().split(",")));
            if (node.biomes.size() == 0) {
                Main.logError("Could not parse set biome node. Did you provide a biome?", parserState, null);
            }
            return node;
        } catch (Exception e) {
            Main.logError("Error parisng biome node. Please check your syntax.", parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            if (!(state.getCurrentBlock().block.getChunk().isLoaded())) {
                state.getCurrentBlock().block.getChunk().load(true);
            }
            String currBiome = state.getCurrentBlock().block.getBiome().toString();
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
