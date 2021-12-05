package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.blockiterator.iterators.MultiIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Splatter extends BrushShape {

    int splatterRadius, sphereCount, sphereRadius;
    String correction = "0.5";
    int argsSeen = 0;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        int spheresGenerated = 0;
        Set<BlockIterator> spheres = new HashSet<>();
        while (spheresGenerated < sphereCount) {
            double xOff = Main.getRand().nextInt((2 * splatterRadius) + 1) - splatterRadius;
            double yOff = Main.getRand().nextInt((2 * splatterRadius) + 1) - splatterRadius;
            double zOff = Main.getRand().nextInt((2 * splatterRadius) + 1) - splatterRadius;
            if (xOff * xOff + yOff * yOff + zOff * zOff < splatterRadius * splatterRadius + 0.5) {
                List<String> argList = new ArrayList<>();
                argList.add(Integer.toString((int) (x + xOff)));
                argList.add(Integer.toString((int) (y + yOff)));
                argList.add(Integer.toString((int) (z + zOff)));
                argList.add(Integer.toString(sphereRadius));
                argList.add(Integer.toString(0));
                argList.add(correction);
                spheres.add(IteratorManager.INSTANCE.getIterator("sphere").newIterator(argList, world, sender));
                spheresGenerated++;
            }

        }
        return ((MultiIterator) IteratorManager.INSTANCE.getIterator("multi")).newIterator(spheres);
    }

    @Override
    public void addNewArgument(String argument) {
        if (argsSeen == 0) {
            splatterRadius = Integer.parseInt(argument);
        }
        if (argsSeen == 1) {
            sphereCount = Integer.parseInt(argument);
        }
        if (argsSeen == 2) {
            sphereRadius = Integer.parseInt(argument);
        }
        if (argsSeen == 3) {
            try {
                Double.parseDouble(argument);
                correction = argument;
            } catch (NumberFormatException e) {
                argsSeen++;
            }
        }
        argsSeen++;
    }

    @Override
    public boolean lastInputProcessed() {
        return argsSeen < 5;
    }

    @Override
    public boolean gotEnoughArgs() {
        return argsSeen <= 2;
    }

    @Override
    public int minArgCount() {
        return 3;
    }

}
