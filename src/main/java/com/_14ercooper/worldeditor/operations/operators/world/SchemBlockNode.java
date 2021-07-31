package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

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
    public boolean getBlock(OperatorState state) {
        state.getOtherValues().put("BlockMaterial", state.getCurrentBlock().otherArgs.get(0));
        state.getOtherValues().put("BlockData", state.getCurrentBlock().otherArgs.get(1));
        state.getOtherValues().put("BlockNbt", state.getCurrentBlock().otherArgs.get(2));
        return true;
    }

    // Check if it's the correct block
    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        if (!isInSet) {
            List<String> stateArgs = new ArrayList<>(state.getCurrentBlock().otherArgs);
            BlockState stateBlock = state.getCurrentWorld().getBlockAt(14, 0, 14).getState();
            BlockWrapper currBlock = state.getCurrentBlock();
            boolean retVal = false;
            try {
                state.setCurrentBlock(state.getCurrentWorld().getBlockAt(14, 0, 14));
                state.getCurrentBlock().otherArgs.addAll(stateArgs);
                state.getCurrentBlock().block.setType(Material.matchMaterial(state.getCurrentBlock().otherArgs.get(0)));
                state.getCurrentBlock().block.setBlockData(Bukkit.getServer().createBlockData(state.getCurrentBlock().otherArgs.get(1)));
                retVal = arg.performNode(state, true);
            } catch (Exception e) {
                Main.logError("Could not perform schem block node", state.getCurrentPlayer(), e);
            } finally {
                state.getCurrentBlock().block.setType(stateBlock.getType());
                state.getCurrentBlock().block.setBlockData(stateBlock.getBlockData());
                state.setCurrentBlock(currBlock);
                state.getCurrentBlock().otherArgs.addAll(stateArgs);
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
