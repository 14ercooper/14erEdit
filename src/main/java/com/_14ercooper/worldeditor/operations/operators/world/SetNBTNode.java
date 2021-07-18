package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class SetNBTNode extends Node {

    String nbt;

    @Override
    public SetNBTNode newNode(CommandSender currentPlayer) {
        SetNBTNode node = new SetNBTNode();
        try {
            node.nbt = GlobalVars.operationParser.parseStringNode(currentPlayer).contents;
        } catch (Exception e) {
            Main.logError("Error creating set NBT node. Please check your syntax.", currentPlayer, e);
            return null;
        }
        if (node.nbt.isEmpty()) {
            Main.logError("Could not parse set NBT node. Requires NBT, but did not find nay.", currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            String command = "data merge block ";
            command += state.getCurrentBlock().getLocation().getBlockX() + " ";
            command += state.getCurrentBlock().getLocation().getBlockY() + " ";
            command += state.getCurrentBlock().getLocation().getBlockZ() + " ";
            command += nbt.replaceAll("_", " ");
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
            return true;
        } catch (Exception e) {
            Main.logError("Error performing set NBT node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
