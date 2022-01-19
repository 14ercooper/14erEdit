// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.query.BlockAtNode;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SetNode extends Node {

    public BlockNode arg;

    @Override
    public boolean isNextNodeBlock() {
        return true;
    }

    @Override
    public SetNode newNode(ParserState parserState) {
        SetNode node = new SetNode();
        try {
            parserState.setInSetNode(true);
            node.arg = (BlockNode) Parser.parsePart(parserState);
            parserState.setInSetNode(false);
        } catch (Exception e) {
            Main.logError("Error parsing set block node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg == null) {
            Main.logError("Could not create set block node. A block is required, but not provided.",
                    parserState, null);
        }
        return node;
    }

    public SetNode newNode(BlockNode arg) {
        SetNode node = new SetNode();
        node.arg = arg;
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            // Block at nodes are handled specially
            arg.getBlock(state);
            if (arg instanceof BlockAtNode) {
                // Set material and data
                SetBlock.setMaterial(state.getCurrentBlock().block, Material.matchMaterial(state.getOtherValues().get("BlockMaterial")),
                        Bukkit.getServer().createBlockData(state.getOtherValues().get("BlockData")),
                        state.getIgnoringPhysics(), state.getCurrentUndo(), state.getCurrentPlayer());
                // Clone NBT (if there is any)
                String nbt = state.getOtherValues().get("BlockNbt");
                if (nbt.length() > 2) {
                    String command = "data merge block " + state.getCurrentBlock().block.getX() + " "
                            + state.getCurrentBlock().block.getY() + " " + state.getCurrentBlock().block.getZ() + " " + nbt;
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                }
                return true;
            }

            Material storedMaterial = state.getCurrentBlock().block.getType();
            String storedData = state.getCurrentBlock().block.getBlockData().getAsString();
            String setMaterial = state.getOtherValues().get("BlockMaterial");
            String setData = state.getOtherValues().get("BlockData");
            // Case same
            if (setMaterial.equalsIgnoreCase("same")) {
                return true;
            }
            // Case only data to set, do the data merge
            if (setMaterial.equalsIgnoreCase("dataonly")) {
                BlockState oldBS = state.getCurrentBlock().block.getState();
                // Step 1, convert the old data into a map
                Map<String, String> oldData = new HashMap<>();
                if (storedData.split("\\[").length < 2) {
                    // This block doesn't support block data
                    return true;
                }
                for (String s : storedData.split("\\[")[1].replaceAll("]", "").split(",")) {
                    oldData.put(s.split("=")[0], s.split("=")[1]);
                }
                // Step 2, merge the new data into the map
                for (String s : setData.split("\\[")[0].replaceAll("]", "").split(",")) {
                    oldData.remove(s.split("=")[0]);
                    oldData.put(s.split("=")[0], s.split("=")[1]);
                }
                // Step 3, rebuild the data and set
                StringBuilder newData = new StringBuilder(storedMaterial.toString().toLowerCase() + "[");
                for (Entry<String, String> e : oldData.entrySet()) {
                    newData.append(e.getKey()).append("=").append(e.getValue()).append(",");
                }
                newData = new StringBuilder(newData.substring(0, newData.length() - 1));
                newData.append("]");
                SetBlock.setMaterial(state.getCurrentBlock().block, state.getCurrentBlock().block.getType(), Bukkit.getServer().createBlockData(newData.toString()),
                        state.getIgnoringPhysics(), state.getCurrentUndo(), state.getCurrentPlayer());
                state.getCurrentUndo().addBlock(oldBS, state.getCurrentBlock().block.getState());
                return storedMaterial == Material.matchMaterial(setMaterial) && !storedData.equalsIgnoreCase(setData);
            }
            // Case NBT only, merge nbt
            else if (setMaterial.equalsIgnoreCase("nbtonly")) {
                String command = "data merge block " + state.getCurrentBlock().block.getX() + " " + state.getCurrentBlock().block.getY()
                        + " " + state.getCurrentBlock().block.getZ();
                command = command + " " + state.getOtherValues().get("BlockNbt");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                return true;
            }
            // Case no data to set
            else if (setData == null) {

                BlockData data = null;
                try {
                    if (!setMaterial.contains("minecraft:"))
                        setMaterial = "minecraft:" + setMaterial;
                    String newData = setMaterial + "[" + storedData.split("\\[")[1];
                    data = Bukkit.getServer().createBlockData(newData);
                    state.getCurrentBlock().block.setBlockData(data, state.getIgnoringPhysics());
                } catch (Exception e) {
                    // Nothing needs to be done, new block can't take the existing data so no
                    // worries
                }

                // Try carry over data
                SetBlock.setMaterial(state.getCurrentBlock().block, Material.matchMaterial(setMaterial), data,
                        state.getIgnoringPhysics(), state.getCurrentUndo(), state.getCurrentPlayer());
                return storedMaterial == Material.matchMaterial(setMaterial);
            }
            // If both provided
            else {
                SetBlock.setMaterial(state.getCurrentBlock().block, Material.matchMaterial(setMaterial),
                        Bukkit.getServer().createBlockData(setData),
                        state.getIgnoringPhysics(), state.getCurrentUndo(), state.getCurrentPlayer());
                return storedMaterial == Material.matchMaterial(setMaterial)
                        && !storedData.equalsIgnoreCase(setData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Main.logError("Error performing block set node, async queue has been dropped. Please check your syntax.", state.getCurrentPlayer(), e);
            AsyncManager.dropAsync();
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
