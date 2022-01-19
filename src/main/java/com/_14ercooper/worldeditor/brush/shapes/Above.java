// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.player.PlayerManager;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Above extends BrushShape {

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) PlayerManager.getPlayerWrapper(sender).getMaxEditY()));
        argList.add(Integer.toString((int) z));
        return IteratorManager.INSTANCE.getIterator("cube").newIterator(argList, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        // Does nothing
    }

    @Override
    public boolean lastInputProcessed() {
        return false;
    }

    @Override
    public boolean gotEnoughArgs() {
        return false;
    }

    @Override
    public int minArgCount() {
        return 0;
    }

}
