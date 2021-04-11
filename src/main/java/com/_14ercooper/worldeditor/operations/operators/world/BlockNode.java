package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.LinkedList;
import java.util.List;

public class BlockNode extends Node {

    // Stores this node's argument
    public List<BlockInstance> blockList;
    public List<String> textMasks;
    public BlockInstance nextBlock;

    // Creates a new node
    @Override
    public BlockNode newNode() {
        BlockNode node = new BlockNode();
        String[] data = GlobalVars.operationParser.parseStringNode().getText().split(";");
        try {
            if (loadBlockList(node, data)) return null;
        } catch (Exception e) {
            Main.logError("Could not parse block node. Block name required, but not found: " + data, Operator.currentPlayer, e);
            return null;
        }
        return node;
    }

    private boolean loadBlockList(BlockNode node, String[] data) {
        node.blockList = new LinkedList<>();
        node.textMasks = new LinkedList<>();
        for (String s : data) {
            if (s.contains("#")) {
                Main.logDebug("Created text mask");
                node.textMasks.add(s.replaceAll("#", ""));
            } else {
                Main.logDebug("Created block instance");
                if (!s.isBlank())
                node.blockList.add(new BlockInstance(s));
            }
        }
        if (node.blockList.isEmpty() && node.textMasks.isEmpty()) {
            Main.logError("Error creating block node. No blocks were provided.", Operator.currentPlayer, null);
            return true;
        }
        return false;
    }

    public BlockNode newNode(String input) {
        BlockNode node = new BlockNode();
        try {
            String[] data = input.split(";");
            if (loadBlockList(node, data)) return null;
        } catch (Exception e) {
            Main.logError("Could not parse block node from input. Block name required, but not found: " + input, Operator.currentPlayer, e);
            return null;
        }
        return node;
    }

    // Return the material this node references
    public String getBlock() {
        try {
            nextBlock = (new BlockInstance()).GetRandom(blockList);
        } catch (Exception e) {
            Main.logError("Error performing block node. Does it contain blocks?", Operator.currentPlayer, e);
            return null;
        }
        return nextBlock.mat;
    }

    // Get the data of this block
    public String getData() {
        try {
            return nextBlock.data;
        } catch (Exception e) {
            return null;
        }
    }

    // Get the NBT of this block
    public String getNBT() {
        return nextBlock.nbt;
    }

    // Check if it's the correct block
    @Override
    public boolean performNode() {
        try {
            return (new BlockInstance()).Contains(blockList, textMasks, Operator.currentBlock);
        } catch (Exception e) {
            Main.logError("Error performing block node. Does it contain blocks?", Operator.currentPlayer, e);
            return false;
        }
    }

    // Returns how many arguments this node takes
    @Override
    public int getArgCount() {
        return 1;
    }

    // Nested class to make parsing , and % lists easier
    private static class BlockInstance {
        String mat;
        String data;
        String nbt = "";
        int weight;

        // Construct a new block instance from an input string
        BlockInstance(String input) {
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
                    inputBuilder.append(" ").append(GlobalVars.operationParser.parseStringNode().getText());
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
                        if (Material.matchMaterial(bi.mat) == testMat)
                            return true;
                    } catch (Exception e) {
                        // No material found. That's okay
                    }
                    if (bi.mat.equalsIgnoreCase("dataonly")) {
                        if (b.getBlockData().getAsString().toLowerCase()
                                .contains(bi.data.replaceAll("[\\[\\]]", "").toLowerCase()))
                            return true;
                    }
                    if (bi.mat.equalsIgnoreCase("nbtonly")) {
                        NBTExtractor nbt = new NBTExtractor();
                        if (nbt.getNBT(b).contains(bi.nbt.replaceAll("[{}]", "")))
                            return true;
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
            int randNum = GlobalVars.rand.nextInt(totalWeight + 1);
            for (BlockInstance bi : list) {
                randNum -= bi.weight;
                if (randNum <= 0)
                    return bi;
            }
            return null;
        }
    }
}
