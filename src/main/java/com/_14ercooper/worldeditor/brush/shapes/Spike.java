// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.brush.BrushShape;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Spike extends BrushShape {

    String height, baseSizeMax, baseSizeMin = "0";
    int gotArgs = 0;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> args = new ArrayList<>();
        args.add(Integer.toString((int) x));
        args.add(Integer.toString((int) y));
        args.add(Integer.toString((int) z));
        args.add(baseSizeMax);
        args.add(baseSizeMin);
        args.add(height);
        args.add(Double.toString(Brush.currentPlayer.getLocation().getX()));
        args.add(Double.toString(Brush.currentPlayer.getLocation().getY()));
        args.add(Double.toString(Brush.currentPlayer.getLocation().getZ()));
        return IteratorManager.INSTANCE.getIterator("spike").newIterator(args, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        if (gotArgs == 0) {
            height = argument;
        } else if (gotArgs == 1) {
            baseSizeMax = argument;
        } else if (gotArgs == 2) {
            try {
                Double.parseDouble(argument);
                baseSizeMin = argument;
            } catch (NumberFormatException e) {
                // This is fine *fire everywhere*
                gotArgs++;
            }
        }
        gotArgs++;
    }

    @Override
    public boolean lastInputProcessed() {
        return gotArgs < 4;
    }

    @Override
    public boolean gotEnoughArgs() {
        return gotArgs <= 1;
    }

    @Override
    public int minArgCount() {
        return 2;
    }

}
