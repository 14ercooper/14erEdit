package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.blockiterator.iterators.FloodfillIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Floodfill extends BrushShape {

    private boolean gotArg = false;
    private int seenArgs = 0;
    private String depth;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z, World world, CommandSender sender) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        argList.add(depth);
        return IteratorManager.INSTANCE.getIterator("floodfill").newIterator(argList, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        if (!gotArg) {
            depth = argument;
        }
        gotArg = true;
        seenArgs++;
    }

    @Override
    public boolean lastInputProcessed() {
        return seenArgs <= 1;
    }

    @Override
    public boolean gotEnoughArgs() {
        return !gotArg;
    }

    @Override
    public int minArgCount() {
        return 1;
    }

    @Override
    public int operatorCount() {
        return 2;
    }

    @Override
    public void runBrush(List<Operator> operators, double x, double y, double z, Player currentPlayer) {
        // Build an array of all blocks to operate on
        BlockIterator blockArray = GetBlocks(x, y, z, currentPlayer.getWorld(), currentPlayer);
        blockArray.setObjectArgs("FloodfillCondition", operators.get(0));

        if (blockArray.getTotalBlocks() == 0) {
            return;
        }
        Main.logDebug("Block array size is " + blockArray.getTotalBlocks()); // -----

        AsyncManager.scheduleEdit(operators.get(1), currentPlayer, blockArray);
    }
}
