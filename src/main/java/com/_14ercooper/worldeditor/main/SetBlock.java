package com._14ercooper.worldeditor.main;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Leaves;

import com._14ercooper.worldeditor.operations.Operator;

public class SetBlock {
    public static void setMaterial(Block b, Material mat) {
	if (mat == null) {
	    Main.logError("Invalid block ID provided. The async queue has been dropped.", Operator.currentPlayer);
	    GlobalVars.asyncManager.dropAsync();
	    return;
	}
	if (GlobalVars.currentUndo != null)
	    GlobalVars.currentUndo.storeBlock(b);
	b.setType(mat, false);
	if (mat.toString().toLowerCase().contains("leaves")) {
	    Leaves leafData = (Leaves) b.getBlockData();
	    leafData.setPersistent(true);
	    b.setBlockData(leafData);
	}
    }

    public static void setMaterial(Block b, Material mat, boolean physics) {
	if (mat == null) {
	    Main.logError("Invalid block ID provided. The async queue has been dropped.", Operator.currentPlayer);
	    GlobalVars.asyncManager.dropAsync();
	    return;
	}
	if (GlobalVars.currentUndo != null)
	    GlobalVars.currentUndo.storeBlock(b);
	b.setType(mat, physics);
	if (mat.toString().toLowerCase().contains("leaves")) {
	    Leaves leafData = (Leaves) b.getBlockData();
	    leafData.setPersistent(true);
	    b.setBlockData(leafData);
	}
    }

    public static void setMaterial(BlockState b, Material mat) {
	if (mat == null) {
	    Main.logError("Invalid block ID provided. The async queue has been dropped.", Operator.currentPlayer);
	    GlobalVars.asyncManager.dropAsync();
	    return;
	}
	if (GlobalVars.currentUndo != null)
	    GlobalVars.currentUndo.storeBlock(b.getBlock());
	b.setType(mat);
	if (mat.toString().toLowerCase().contains("leaves")) {
	    Leaves leafData = (Leaves) b.getBlockData();
	    leafData.setPersistent(true);
	    b.setBlockData(leafData);
	}
    }
}
