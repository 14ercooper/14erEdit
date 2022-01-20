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

package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BlockNode extends Node {

    // Stores this node's argument
    public List<BlockInstance> blockList;
    public List<String> textMasks;

    // Creates a new node
    @Override
    public BlockNode newNode(ParserState parserState) {
        BlockNode node = new BlockNode();
        String[] data = Parser.parseStringNode(parserState).getText().split(";");
        try {
            if (loadBlockList(node, data, parserState)) return null;
        } catch (Exception e) {
            Main.logError("Could not parse block node. Block name required, but not found: " + Arrays.toString(data), parserState.getCurrentPlayer(), e);
            return null;
        }
        return node;
    }

    private boolean loadBlockList(BlockNode node, String[] data, ParserState parserState) {
        node.blockList = new LinkedList<>();
        node.textMasks = new LinkedList<>();
        for (String s : data) {
            if (s.contains("#")) {
                Main.logDebug("Created text mask");
                node.textMasks.add(s.replaceAll("#", ""));
            } else {
                Main.logDebug("Created block instance");
                if (!s.isBlank())
                    node.blockList.add(new BlockInstance(s, parserState));
            }
        }
        if (node.blockList.isEmpty() && node.textMasks.isEmpty()) {
            Main.logError("Error creating block node. No blocks were provided.", parserState.getCurrentPlayer(), null);
            return true;
        }
        return false;
    }

    public BlockNode newNode(String input, ParserState parserState) {
        BlockNode node = new BlockNode();
        try {
            String[] data = input.split(";");
            if (loadBlockList(node, data, parserState)) return null;
        } catch (Exception e) {
            Main.logError("Could not parse block node from input. Block name required, but not found: " + input, parserState.getCurrentPlayer(), e);
            return null;
        }
        return node;
    }

    // Return the material this node references
    public boolean getBlock(OperatorState state) {
        try {
            BlockInstance nextBlock = (new BlockInstance()).GetRandom(blockList);
            state.getOtherValues().put("BlockMaterial", nextBlock.mat);
            state.getOtherValues().put("BlockData", nextBlock.data);
            state.getOtherValues().put("BlockNbt", nextBlock.nbt);
            return true;
        } catch (Exception e) {
            Main.logError("Error performing block node. Does it contain blocks?", state.getCurrentPlayer(), e);
            return false;
        }
    }

    // Check if it's the correct block
    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            return (new BlockInstance()).Contains(blockList, textMasks, state.getCurrentBlock().block);
        } catch (Exception e) {
            Main.logError("Error performing block node. Does it contain blocks?", state.getCurrentPlayer(), e);
            return false;
        }
    }

    // Returns how many arguments this node takes
    @Override
    public int getArgCount() {
        return 1;
    }

    // True if the second string's block data node entirely contains the first
    public static boolean blockDataStringsContained(String first, String second) {
        String firstString = first.replaceAll("[\\[\\]]", "").toLowerCase();
        String secondString = second.toLowerCase();
        String[] firstContents = firstString.split(",");
        for (String s : firstContents) {
            if (!secondString.contains(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean blockDataStringsMatch(String first, String second) {
        String firstString = first.split("\\[]")[first.split("\\[]").length - 1].replaceAll("[\\[\\]]", "").toLowerCase();
        String secondString = second.split("\\[]")[second.split("\\[]").length - 1].replaceAll("[\\[\\]]", "").toLowerCase();
        String[] firstContents = firstString.split(",");
        String[] secondContents = secondString.split(",");
        if (firstContents.length != secondContents.length) {
            return false;
        }
        for (String s : firstContents) {
            if (!secondString.contains(s)) {
                return false;
            }
        }
        return true;
    }

    // Nested class to make parsing , and % lists easier
    public static class BlockInstance {
        public String mat;
        public String data;
        public String nbt = "";
        public int weight;

        // Construct a new block instance from an input string
        BlockInstance(String input, ParserState parserState) {
            if (input.toCharArray()[0] == '[') {
                Main.logDebug("Data only node");
                // Data only
                mat = "dataonly";
                data = input.replaceAll("[\\[\\]]", "");
                weight = 1;
            } else if (input.toCharArray()[0] == '{') {
                Main.logDebug("NBT node");
                // NBT only
                mat = "nbtonly";
                data = null;
                StringBuilder inputBuilder = new StringBuilder(input);
                while (inputBuilder.charAt(inputBuilder.length() - 1) != '}') {
                    inputBuilder.append(" ").append(Parser.parseStringNode(parserState).getText());
                }
                input = inputBuilder.toString();
                nbt = input.replaceAll("[{}]", "");
                weight = 1;
            } else if (input.contains("%")) {
                if (input.contains("[")) {
                    mat = input.split("%")[1].split("\\[")[0];
                    data = input.split("%")[1];
                } else {
                    mat = input.split("%")[1];
                    data = null;
                }
                weight = Integer.parseInt(input.split("%")[0]);
            } else {
                if (input.contains("[")) {
                    mat = input.split("\\[")[0];
                    data = input;
                } else {
                    mat = input;
                    data = null;
                }
                weight = 1;
            }
        }

        // Empty constructor
        BlockInstance() {
        }

        // Does the list contain a certain block (for masking)
        boolean Contains(List<BlockInstance> list, List<String> masks, Block b) {
            Material testMat = b.getType();
            if (!list.isEmpty()) {
                for (BlockInstance bi : list) {
                    try {
                        if (Material.matchMaterial(bi.mat) == testMat) {
                            if (bi.data != null) {
                                if (!blockDataStringsContained(bi.data.split("\\[")[1], b.getBlockData().getAsString())) {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }
                            else {
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        // No material found. That's okay
                    }
                    if (bi.mat.equalsIgnoreCase("dataonly")) {
                        if (blockDataStringsContained(bi.data, b.getBlockData().getAsString())) {
                            return true;
                        }
                    }
                    if (bi.mat.equalsIgnoreCase("nbtonly")) {
                        NBTExtractor nbt = new NBTExtractor();
                        if (nbt.getNBT(b).contains(bi.nbt.replaceAll("[{}]", ""))) {
                            return true;
                        }
                    }
                }
            }
            if (!masks.isEmpty()) {
                String testString = b.getType().toString().toLowerCase();
                for (String mS : masks) {
                    if (testString.contains(mS))
                        return true;
                }
            }
            return false;
        }

        // Get a random block from the list (for setting)
        BlockInstance GetRandom(List<BlockInstance> list) {
            int totalWeight = 0;
            for (BlockInstance bi : list) {
                totalWeight += bi.weight;
            }
            int randNum = Main.getRand().nextInt(totalWeight + 1);
            for (BlockInstance bi : list) {
                randNum -= bi.weight;
                if (randNum <= 0)
                    return bi;
            }
            return null;
        }
    }
}
