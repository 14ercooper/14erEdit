package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FacingNode extends BlockNode {

    BlockNode arg;

    @Override
    public FacingNode newNode(ParserState parserState) {
        FacingNode node = new FacingNode();
        try {
            node.arg = (BlockNode) Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating facing node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg == null) {
            Main.logError("Could not parse facing node. A child operation is required, but not found.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public String getData(OperatorState state) {
        try {
            BlockData dat = Bukkit.getServer().createBlockData(arg.getData(state));

            List<String> dirs = new ArrayList<>();
            Block b = state.getCurrentBlock();
            // And next to a solid block
            if (b.getRelative(BlockFace.NORTH).getType() != Material.AIR) {
                dirs.add("[north=true]");
            }
            if (b.getRelative(BlockFace.EAST).getType() != Material.AIR) {
                dirs.add("[east=true]");
            }
            if (b.getRelative(BlockFace.SOUTH).getType() != Material.AIR) {
                dirs.add("[south=true]");
            }
            if (b.getRelative(BlockFace.WEST).getType() != Material.AIR) {
                dirs.add("[west=true]");
            }
            if (b.getRelative(BlockFace.UP).getType() != Material.AIR) {
                dirs.add("[up=true]");
            }
            if (b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                dirs.add("[up=true]");
            }

            if (dirs.size() > 0) {
                String newData = arg.getBlock(state).toLowerCase(Locale.ROOT);
                newData += dirs.get(GlobalVars.rand.nextInt(dirs.size()));
                BlockData newDat = Bukkit.getServer().createBlockData(newData);
                dat = dat.merge(newDat);
            }

            return dat.getAsString();
        } catch (Exception e) {
            Main.logError("Error performing facing node. Please check your syntax.", state.getCurrentPlayer(), e);
            return null;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
