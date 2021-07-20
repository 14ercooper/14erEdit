package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.world.BlockNode;
import org.bukkit.block.Block;

public class BlockAtNode extends BlockNode {

    NumberNode x, y, z;
    boolean xA = false, yA = false, zA = false;
    Node node;

    @Override
    public BlockAtNode newNode(ParserState parserState) {
        BlockAtNode baNode = new BlockAtNode();
        try {
            baNode.x = Parser.parseNumberNode(parserState);
            baNode.y = Parser.parseNumberNode(parserState);
            baNode.z = Parser.parseNumberNode(parserState);
            baNode.xA = baNode.x.isAbsolute;
            baNode.yA = baNode.y.isAbsolute;
            baNode.zA = baNode.z.isAbsolute;
            try {
                baNode.node = Parser.parsePart(parserState);
            } catch (Exception e) {
                Main.logDebug("Block at created with type blocknode");
            }
        } catch (Exception e) {
            Main.logError("Error creating block at node. Please check your syntax.", parserState, e);
            return null;
        }
        if (baNode.z == null) {
            Main.logError(
                    "Could not parse block at node. Three numbers and optionally an operation are required, but not given.",
                    parserState, null);
        }
        return baNode;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            Block currBlock = state.getCurrentBlock();
            xA = x.isAbsolute;
            yA = y.isAbsolute;
            zA = z.isAbsolute;
            state.setCurrentBlock(state.getCurrentWorld().getBlockAt(
                    x.getInt(state) + (xA ? 0 : currBlock.getX()), y.getInt(state) + (yA ? 0 : currBlock.getY()),
                    z.getInt(state) + (zA ? 0 : currBlock.getZ())));
            boolean matches = node.performNode(state);
            state.setCurrentBlock(currBlock);
            return matches;
        } catch (Exception e) {
            Main.logError("Error performing block at node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    // Return the material this node references
    int xV, yV, zV;

    @Override
    public String getBlock(OperatorState state) {
        xV = x.getInt(state);
        yV = y.getInt(state);
        zV = z.getInt(state);
        xA = x.isAbsolute;
        yA = y.isAbsolute;
        zA = z.isAbsolute;
        return state.getCurrentWorld().getBlockAt(xV + (xA ? 0 : state.getCurrentBlock().getX()),
                yV + (yA ? 0 : state.getCurrentBlock().getY()), zV + (zA ? 0 : state.getCurrentBlock().getZ()))
                .toString();
    }

    // Get the data of this block
    @Override
    public String getData(OperatorState state) {
        return state.getCurrentWorld().getBlockAt(xV + (xA ? 0 : state.getCurrentBlock().getX()),
                yV + (yA ? 0 : state.getCurrentBlock().getY()), zV + (zA ? 0 : state.getCurrentBlock().getZ()))
                .toString();
    }

    // Get the NBT of this block
    @Override
    public String getNBT(OperatorState state) {
        NBTExtractor nbt = new NBTExtractor();
        return nbt.getNBT(state.getCurrentWorld().getBlockAt(xV + (xA ? 0 : state.getCurrentBlock().getX()),
                yV + (yA ? 0 : state.getCurrentBlock().getY()), zV + (zA ? 0 : state.getCurrentBlock().getZ())));
    }

    @Override
    public int getArgCount() {
        return 4;
    }

}
