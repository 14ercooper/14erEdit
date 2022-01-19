// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.brush.BrushShape;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class NewCylinder extends BrushShape {

    String radius, height, dimension, correction = "0.5";
    int gotArgs = 0;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> args = new ArrayList<>();
        args.add(Integer.toString((int) x));
        args.add(Integer.toString((int) y));
        args.add(Integer.toString((int) z));
        args.add(radius);
        args.add(correction);
        args.add(height);
        // Axis X
        if (dimension.equalsIgnoreCase("x")) {
            args.add("0");
            args.add("1");
            args.add("1");
        }
        // Axis Y
        if (dimension.equalsIgnoreCase("y")) {
            args.add("1");
            args.add("0");
            args.add("1");
        }
        // Axis Z
        if (dimension.equalsIgnoreCase("z")) {
            args.add("1");
            args.add("1");
            args.add("0");
        }
        return IteratorManager.INSTANCE.getIterator("newcylinder").newIterator(args, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        if (gotArgs == 0) {
            radius = argument;
        } else if (gotArgs == 1) {
            height = argument;
        } else if (gotArgs == 2) {
            dimension = argument;
        } else if (gotArgs == 3) {
            try {
                Double.parseDouble(argument);
                correction = argument;
            } catch (NumberFormatException e) {
                // This is okay
                gotArgs++;
            }
        }
        gotArgs++;
    }

    @Override
    public boolean lastInputProcessed() {
        return gotArgs < 5;
    }

    @Override
    public boolean gotEnoughArgs() {
        return gotArgs <= 2;
    }

    @Override
    public int minArgCount() {
        return 3;
    }

}
