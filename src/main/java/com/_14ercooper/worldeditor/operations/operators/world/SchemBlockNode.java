package com._14ercooper.worldeditor.operations.operators.world;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com._14ercooper.worldeditor.blockiterator.iterators.SchemBrushIterator;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class SchemBlockNode extends BlockNode {

    // Stores this node's argument
    Node arg;
    boolean isInSet;

    // Creates a new node
    @Override
    public BlockNode newNode() {
	SchemBlockNode node = new SchemBlockNode();
	node.isInSet = Operator.inSetNode;
	if (!node.isInSet) {
	    Main.logDebug("SchemBlockNode: Processing subnode");
	    node.arg = GlobalVars.operationParser.parsePart();
	}
	return node;
    }

    // This should never be run
    @Override
    public BlockNode newNode(String text) {
	Main.logError("Schematic block node in invalid state", Operator.currentPlayer, null);
	return null;
    }

    // Return the material this node references
    @Override
    public String getBlock() {
	return SchemBrushIterator.blockType;
    }

    // Get the data of this block
    @Override
    public String getData() {
	return SchemBrushIterator.blockData;
    }

    // Get the NBT of this block
    @Override
    public String getNBT() {
	return SchemBrushIterator.nbt;
    }

    // Check if it's the correct block
    @Override
    public boolean performNode() {
	if (!isInSet) {
	    BlockState state = Operator.currentPlayer.getWorld().getBlockAt(14, 0, 14).getState();
	    Block currBlock = Operator.currentBlock;
	    boolean retVal = false;
	    try {
		Operator.currentBlock = Operator.currentPlayer.getWorld().getBlockAt(14, 0, 14);
		Operator.currentBlock.setType(Material.matchMaterial(SchemBrushIterator.blockType));
		Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(SchemBrushIterator.blockData));
		retVal = arg.performNode();
	    }
	    catch (Exception e) {
		Main.logError("Could not perform schem block node", Operator.currentPlayer, e);
	    }
	    finally {
		Operator.currentBlock.setType(state.getType());
		Operator.currentBlock.setBlockData(state.getBlockData());
		Operator.currentBlock = currBlock;
	    }
	    return retVal;
	}
	else {
	    return true;
	}
    }

    // Returns how many arguments this node takes
    @Override
    public int getArgCount() {
	return 1;
    }
}
