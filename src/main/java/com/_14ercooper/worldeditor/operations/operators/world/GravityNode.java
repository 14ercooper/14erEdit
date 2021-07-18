package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;

public class GravityNode extends Node {

    @Override
    public GravityNode newNode(CommandSender currentPlayer) {
        return new GravityNode();
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            Material mat = state.getCurrentBlock().getType();
            Block b = state.getCurrentBlock();
            while (b.getX() != 0 && GlobalVars.brushMask.contains(b.getRelative(BlockFace.DOWN).getType())) {
                b = b.getRelative(BlockFace.DOWN);
            }
            b.setType(mat);
            state.getCurrentBlock().setType(Material.AIR);
            return true;
        } catch (Exception e) {
            Main.logError("Error performing gravity node. Please check your syntax (or tell 14er how you got here).",
                    state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 0;
    }

}
