package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.blockiterator.iterators.SchemBrushIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class SchemBlockNode extends BlockNode {

    // Stores this node's argument
    Node arg;
    boolean isInSet;

    // Creates a new node
    @Override
    public BlockNode newNode(ParserState parserState) {
        SchemBlockNode node = new SchemBlockNode();
        node.isInSet = parserState.getInSetNode();
        if (!node.isInSet) {
            Main.logDebug("SchemBlockNode: Processing subnode");
            node.arg = Parser.parsePart(parserState);
        }
        return node;
    }

    // This should never be run
    @Override
    public BlockNode newNode(String text, ParserState parserState) {
        Main.logError("Schematic block node in invalid state", parserState, null);
        return null;
    }

    // Return the material this node references
    @Override
    public String getBlock(OperatorState state) {
        return SchemBrushIterator.blockType;
    }

    // Get the data of this block
    @Override
    public String getData(OperatorState state) {
        return SchemBrushIterator.blockData;
    }

    // Get the NBT of this block
    @Override
    public String getNBT(OperatorState state) {
        return SchemBrushIterator.nbt;
    }

    // Check if it's the correct block
    @Override
    public boolean performNode(OperatorState state) {
        if (!isInSet) {
            BlockState stateBlock = state.getCurrentWorld().getBlockAt(14, 0, 14).getState();
            Block currBlock = state.getCurrentBlock();
            boolean retVal = false;
            try {
                state.setCurrentBlock(state.getCurrentWorld().getBlockAt(14, 0, 14));
                state.getCurrentBlock().setType(Material.matchMaterial(SchemBrushIterator.blockType));
                state.getCurrentBlock().setBlockData(Bukkit.getServer().createBlockData(SchemBrushIterator.blockData));
                retVal = arg.performNode(state);
            } catch (Exception e) {
                Main.logError("Could not perform schem block node", state.getCurrentPlayer(), e);
            } finally {
                state.getCurrentBlock().setType(stateBlock.getType());
                state.getCurrentBlock().setBlockData(stateBlock.getBlockData());
                state.setCurrentBlock(currBlock);
            }
            return retVal;
        } else {
            return true;
        }
    }

    // Returns how many arguments this node takes
    @Override
    public int getArgCount() {
        return 1;
    }
}
