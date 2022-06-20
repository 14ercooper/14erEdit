package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;

import java.util.Set;

public class RotateNode extends Node {

    NumberNode arg;

    @Override
    public RotateNode newNode(ParserState parserState) {
        RotateNode node = new RotateNode();
        node.arg = Parser.parseNumberNode(parserState);
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        int rotateCount = arg.getInt(state);
        for (int i = 0; i < rotateCount; i++) {
            BlockData blockData = state.getCurrentBlock().block.getBlockData();
            if (blockData instanceof Directional) {
                Directional directional = (Directional) blockData;
                BlockFace face = directional.getFacing();
                switch (face) {
                    case NORTH:
                        face = BlockFace.EAST;
                        break;
                    case EAST:
                        face = BlockFace.SOUTH;
                        break;
                    case SOUTH:
                        face = BlockFace.WEST;
                        break;
                    case WEST:
                        face = BlockFace.NORTH;;
                        break;
                    default:
                        face = BlockFace.NORTH;
                        break;
                }
                directional.setFacing(face);
                state.getCurrentBlock().block.setBlockData(directional);
            }
            else if (blockData instanceof MultipleFacing) {
                MultipleFacing multipleFacing = (MultipleFacing) blockData;
                boolean north = false, east = false, south = false, west = false;
                if (multipleFacing.hasFace(BlockFace.NORTH)) {
                    east = true;
                }
                if (multipleFacing.hasFace(BlockFace.EAST)) {
                    south = true;
                }
                if (multipleFacing.hasFace(BlockFace.SOUTH)) {
                    west = true;
                }
                if (multipleFacing.hasFace(BlockFace.WEST)) {
                    north = true;
                }
                multipleFacing.setFace(BlockFace.NORTH, north);
                multipleFacing.setFace(BlockFace.EAST, east);
                multipleFacing.setFace(BlockFace.SOUTH, south);
                multipleFacing.setFace(BlockFace.WEST, west);
                state.getCurrentBlock().block.setBlockData(multipleFacing);
            }
        }
        return true;
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
