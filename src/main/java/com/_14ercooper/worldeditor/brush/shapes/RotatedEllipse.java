package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RotatedEllipse extends BrushShape {

    String hFD, strL, dX, dY, dZ = "";
    int gotArgs = 0;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> args = new ArrayList<>();
        args.add(Integer.toString((int) x));
        args.add(Integer.toString((int) y));
        args.add(Integer.toString((int) z));
        args.add(hFD);
        args.add(strL);
        if (dZ.isEmpty()) {
            args.add(Double.toString((Main.getRand().nextDouble() * 2) - 1));
            args.add(Double.toString((Main.getRand().nextDouble() * 2) - 1));
            args.add(Double.toString((Main.getRand().nextDouble() * 2) - 1));
        } else {
            args.add(dX);
            args.add(dY);
            args.add(dZ);
        }
        return IteratorManager.INSTANCE.getIterator("rotatedellipse").newIterator(args, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        if (gotArgs == 0) {
            hFD = argument;
        } else if (gotArgs == 1) {
            strL = argument;
        } else if (gotArgs == 2) {
            try {
                Double.parseDouble(argument);
                dX = argument;
            } catch (NumberFormatException e) {
                // This is okay
                gotArgs = 99;
            }
        } else if (gotArgs == 3) {
            dY = argument;
        } else if (gotArgs == 4) {
            dZ = argument;
        }
        gotArgs++;
    }

    @Override
    public boolean lastInputProcessed() {
        return gotArgs < 6;
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
