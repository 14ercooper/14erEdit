package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.Operator;
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
    public SetNode newNode() {
        SetNode node = new SetNode();
        try {
            Operator.inSetNode = true;
            node.arg = (BlockNode) GlobalVars.operationParser.parsePart();
            Operator.inSetNode = false;
        } catch (Exception e) {
            Main.logError("Error parsing set block node. Please check your syntax.", Operator.currentPlayer, e);
            return null;
        }
        if (node.arg == null) {
            Main.logError("Could not create set block node. A block is required, but not provided.",
                    Operator.currentPlayer, null);
        }
        return node;
    }

    public SetNode newNode(BlockNode arg) {
        SetNode node = new SetNode();
        node.arg = arg;
        return node;
    }

    @Override
    public boolean performNode() {
        try {
            // Block at nodes are handled specially
            if (arg instanceof BlockAtNode) {
                // Set material
                SetBlock.setMaterial(Operator.currentBlock, Material.matchMaterial(arg.getBlock()),
                        Operator.ignoringPhysics, GlobalVars.asyncManager.currentAsyncOp.getUndo());
                // Set data
                Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(arg.getData()),
                        Operator.ignoringPhysics);
                // Clone NBT (if there is any)
                String nbt = arg.getNBT();
                if (nbt.length() > 2) {
                    String command = "data merge block " + Operator.currentBlock.getX() + " "
                            + Operator.currentBlock.getY() + " " + Operator.currentBlock.getZ() + " " + nbt;
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                }
                return true;
            }

            Material storedMaterial = Operator.currentBlock.getType();
            String storedData = Operator.currentBlock.getBlockData().getAsString();
            String setMaterial = arg.getBlock();
            String setData = arg.getData();
            // Case same
            if (setMaterial.equalsIgnoreCase("same")) {
                return true;
            }
            // Case only data to set, do the data merge
            if (setMaterial.equalsIgnoreCase("dataonly")) {
                BlockState oldBS = Operator.currentBlock.getState();
                // Step 1, convert the old data into a map
                Map<String, String> oldData = new HashMap<>();
                if (storedData.split("\\[").length < 2) {
                    // This block doesn't support block data
                    return true;
                }
                for (String s : storedData.split("\\[")[1].replaceAll("\\]", "").split(",")) {
                    oldData.put(s.split("=")[0], s.split("=")[1]);
                }
                // Step 2, merge the new data into the map
                for (String s : setData.split("\\[")[0].replaceAll("\\]", "").split(",")) {
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
                Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(newData.toString()),
                        Operator.ignoringPhysics);
                GlobalVars.asyncManager.currentAsyncOp.getUndo().addBlock(oldBS, Operator.currentBlock.getState());
                return storedMaterial == Material.matchMaterial(setMaterial) && !storedData.equalsIgnoreCase(setData);
            }
            // Case NBT only, merge nbt
            else if (setMaterial.equalsIgnoreCase("nbtonly")) {
                String command = "data merge block " + Operator.currentBlock.getX() + " " + Operator.currentBlock.getY()
                        + " " + Operator.currentBlock.getZ();
                command = command + " " + arg.getNBT();
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                return true;
            }
            // Case no data to set
            else if (setData == null) {
                // Try carry over data
                SetBlock.setMaterial(Operator.currentBlock, Material.matchMaterial(setMaterial),
                        Operator.ignoringPhysics, GlobalVars.asyncManager.currentAsyncOp.getUndo());
                try {
                    if (!setMaterial.contains("minecraft:"))
                        setMaterial = "minecraft:" + setMaterial;
                    String newData = setMaterial + "[" + storedData.split("\\[")[1];
                    BlockData data = Bukkit.getServer().createBlockData(newData);
                    Operator.currentBlock.setBlockData(data, Operator.ignoringPhysics);
                } catch (Exception e) {
                    // Nothing needs to be done, new block can't take the existing data so no
                    // worries
                }
                return storedMaterial == Material.matchMaterial(setMaterial);
            }
            // If both provided
            else {
                // Case materials match (update data) - this is slightly faster in some cases
                if (storedMaterial == Material.matchMaterial(setMaterial)) {
                    Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(setData),
                            Operator.ignoringPhysics);
                    return !storedData.equalsIgnoreCase(setData);
                }
                // Case materials don't match (set all)
                else {
                    SetBlock.setMaterial(Operator.currentBlock, Material.matchMaterial(setMaterial),
                            Operator.ignoringPhysics, GlobalVars.asyncManager.currentAsyncOp.getUndo());
                    Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(setData),
                            Operator.ignoringPhysics);
                    return storedMaterial == Material.matchMaterial(setMaterial)
                            && !storedData.equalsIgnoreCase(setData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Main.logError("Error performing block set node. Please check your syntax.", Operator.currentPlayer, e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
