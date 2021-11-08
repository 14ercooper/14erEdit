package com._14ercooper.worldeditor.brush;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class BrushShape {
    public abstract BlockIterator GetBlocks(double x, double y, double z, World world, CommandSender sender);

    public abstract void addNewArgument(String argument);

    public abstract boolean lastInputProcessed();

    public abstract boolean gotEnoughArgs();

    public abstract int minArgCount();

    public int operatorCount() {
        return 1;
    }

    public void runBrush(List<Operator> operators, double x, double y, double z, Player currentPlayer) {
        // Build an array of all blocks to operate on
        BlockIterator blockArray = GetBlocks(x, y, z, currentPlayer.getWorld(), currentPlayer);

        if (blockArray == null || blockArray.getTotalBlocks() == 0) {
            return;
        }
        Main.logDebug("Block array size is " + blockArray.getTotalBlocks()); // -----

        AsyncManager.scheduleEdit(operators.get(0), currentPlayer, blockArray);
    }
}
