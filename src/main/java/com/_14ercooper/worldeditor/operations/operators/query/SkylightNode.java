package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;

public class SkylightNode extends Node {

    NumberNode arg;

    @Override
    public SkylightNode newNode(CommandSender currentPlayer) {
        SkylightNode node = new SkylightNode();
        node.arg = GlobalVars.operationParser.parseNumberNode(currentPlayer);
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        BlockFace[] faces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST,
                BlockFace.WEST};
        int light = state.getCurrentBlock().getLightFromSky();
        for (BlockFace face : faces) {
            int l = state.getCurrentBlock().getRelative(face).getLightFromSky();
            if (l > light)
                light = l;
        }
        return light >= arg.getValue(state);
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
