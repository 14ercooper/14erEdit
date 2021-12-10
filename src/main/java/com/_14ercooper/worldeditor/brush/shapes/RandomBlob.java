package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RandomBlob extends BrushShape {

    int radiusMin, radiusMax;
    int vectorCountMin = 2, vectorCountMax = 5;
    String vectorAmplitude = "1.4";
    String radiusBoost = "0.2";
    String radiusCorrection = "0.5";
    int numArgsProcessed = 0;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        int radius = Main.randRange(radiusMin, radiusMax);
        int vectorCount = Main.randRange(vectorCountMin, vectorCountMax);
        argList.add(Integer.toString(radius));
        argList.add(Integer.toString(vectorCount));
        argList.add(vectorAmplitude);
        argList.add(radiusBoost);
        argList.add(radiusCorrection);
        return IteratorManager.INSTANCE.getIterator("blob").newIterator(argList, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        if (numArgsProcessed == 0) {
            try {
                radiusMin = Integer.parseInt(argument);
            }
            catch (NumberFormatException e) {
                numArgsProcessed = 99;
            }
        }
        else if (numArgsProcessed == 1) {
            try {
                radiusMax = Integer.parseInt(argument);
            }
            catch (NumberFormatException e) {
                numArgsProcessed = 99;
            }
        }
        else if (numArgsProcessed == 2) {
            try {
                vectorCountMin = Integer.parseInt(argument);
            }
            catch (NumberFormatException e) {
                // Not a number, start operation parser
                numArgsProcessed = 99;
            }
        }
        else if (numArgsProcessed == 3) {
            try {
                vectorCountMax = Integer.parseInt(argument);
            }
            catch (NumberFormatException e) {
                // Not a number, start operation parser
                numArgsProcessed = 99;
            }
        }
        else if (numArgsProcessed == 4) {
            try {
                Double.parseDouble(argument);
                vectorAmplitude = argument;
            }
            catch (NumberFormatException e) {
                // Not a number, start operation parser
                numArgsProcessed = 99;
            }
        }
        else if (numArgsProcessed == 5) {
            try {
                Double.parseDouble(argument);
                radiusBoost = argument;
            }
            catch (NumberFormatException e) {
                // Not a number, start operation parser
                numArgsProcessed = 99;
            }
        }
        else if (numArgsProcessed == 6) {
            try {
                Double.parseDouble(argument);
                radiusCorrection = argument;
            }
            catch (NumberFormatException e) {
                // Not a number, start operation parser
                numArgsProcessed = 99;
            }
        }
        numArgsProcessed++;
    }

    @Override
    public boolean lastInputProcessed() {
        return numArgsProcessed < 8;
    }

    @Override
    public boolean gotEnoughArgs() {
        return numArgsProcessed <= 0;
    }

    @Override
    public int minArgCount() {
        return 2;
    }
}
