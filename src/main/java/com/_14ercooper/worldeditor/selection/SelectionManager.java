package com._14ercooper.worldeditor.selection;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.GlobalVars;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SelectionManager {
    protected final int[] clipboardOffset = {0, 0, 0};
    private double[] positionOne = {-1.0, -1.0, -1.0};
    private double[] positionTwo = {-1.0, -1.0, -1.0};
    protected String mirrorString = "";
    protected int executionOrder = 0;
    // These two variables make the world editing math a bit easier
    private double[] mostNegativeCorner = new double[3];
    private double[] mostPositiveCorner = new double[3];

    public static SelectionManager getSelectionManager(UUID player) {
        SelectionWand wand = null;
        for (SelectionWand sw : SelectionWandListener.wands) {
            if (sw.owner.equals(player))
                wand = sw;
        }
        if (wand != null) {
            return wand.manager;
        } else {
            return null;
        }
    }

    public boolean updatePositionOne(double x, double y, double z, UUID player) {
        positionOne[0] = x;
        // Use nested ternary operators to clamp y between 0 and 255
        positionOne[1] = y < 0 ? 0 : y > 255 ? 255 : y;
        positionOne[2] = z;
        getPlayer(player).sendMessage("§dFirst position updated to (" + x + ", " + y + ", "
                + z + "); giving a volume of "
                + (Math.abs(positionOne[0] - positionTwo[0]) + 1)
                * (Math.abs(positionOne[1] - positionTwo[1]) + 1)
                * (Math.abs(positionOne[2] - positionTwo[2]) + 1));
        recalculateCorners();
        return true;
    }

    public boolean updatePositionTwo(double x, double y, double z, UUID player) {
        positionTwo[0] = x;
        // Use nested ternary operators to clamp y between 0 and 255
        positionTwo[1] = y < 0 ? 0 : y > 255 ? 255 : y;
        positionTwo[2] = z;
        getPlayer(player).sendMessage("§dSecond position updated to (" + x + ", " + y + ", "
                + z + "); giving a volume of "
                + (Math.abs(positionOne[0] - positionTwo[0]) + 1)
                * (Math.abs(positionOne[1] - positionTwo[1]) + 1)
                * (Math.abs(positionOne[2] - positionTwo[2]) + 1));
        recalculateCorners();
        return true;
    }

    // Check both selection positions have been defined (create a valid region)
    public boolean regionDefined() {
        if (positionOne[0] == -1.0 && positionOne[1] == -1.0 && positionOne[2] == -1.0)
            return false;
        return positionTwo[0] != -1.0 || positionTwo[1] != -1.0 || positionTwo[2] != -1.0;
    }

    // This recalculates the two corners to help make the math a bit easier
    private void recalculateCorners() {
        // Check both selection positions have been defined first
        if (!regionDefined())
            return;

        // Set the X, then Y, then Z
        if (positionOne[0] <= positionTwo[0]) {
            mostNegativeCorner[0] = positionOne[0];
            mostPositiveCorner[0] = positionTwo[0];
        } else {
            mostNegativeCorner[0] = positionTwo[0];
            mostPositiveCorner[0] = positionOne[0];
        }

        if (positionOne[1] <= positionTwo[1]) {
            mostNegativeCorner[1] = positionOne[1];
            mostPositiveCorner[1] = positionTwo[1];
        } else {
            mostNegativeCorner[1] = positionTwo[1];
            mostPositiveCorner[1] = positionOne[1];
        }

        if (positionOne[2] <= positionTwo[2]) {
            mostNegativeCorner[2] = positionOne[2];
            mostPositiveCorner[2] = positionTwo[2];
        } else {
            mostNegativeCorner[2] = positionTwo[2];
            mostPositiveCorner[2] = positionOne[2];
        }
    }

    // Getter for the position
    public double[] getMostNegativeCorner() {
        return mostNegativeCorner;
    }

    // Getter for the position
    public double[] getMostPositiveCorner() {
        return mostPositiveCorner;
    }

    // Getter for the position
    public double[] getPositionOne() {
        return positionOne;
    }

    // Getter for the position
    public double[] getPositionTwo() {
        return positionTwo;
    }

    // Expands the selection in direction by amt
    public boolean expandSelection(double amt, String direction, UUID player) {
        if (direction.equalsIgnoreCase("north")) { // -z
            mostNegativeCorner[2] = mostNegativeCorner[2] - amt;
            expandSelectionMessage(player);
            return true;
        } else if (direction.equalsIgnoreCase("south")) { // +z
            mostPositiveCorner[2] = mostPositiveCorner[2] + amt;
            expandSelectionMessage(player);
            return true;
        } else if (direction.equalsIgnoreCase("east")) { // +x
            mostPositiveCorner[0] = mostPositiveCorner[0] + amt;
            expandSelectionMessage(player);
            return true;
        } else if (direction.equalsIgnoreCase("west")) { // -x
            mostNegativeCorner[0] = mostNegativeCorner[0] - amt;
            expandSelectionMessage(player);
            return true;
        } else if (direction.equalsIgnoreCase("up")) { // +y
            mostPositiveCorner[1] = mostPositiveCorner[1] + amt;
            expandSelectionMessage(player);
            return true;
        } else if (direction.equalsIgnoreCase("down")) { // -y
            mostNegativeCorner[1] = mostNegativeCorner[1] - amt;
            expandSelectionMessage(player);
            return true;
        }
        return false;
    }

    // Message for the selection expansion
    private void expandSelectionMessage(UUID player) {
        getPlayer(player).sendMessage("§dRegion expanded to " + Math
                .abs(mostNegativeCorner[0] - mostPositiveCorner[0] * Math.signum(mostPositiveCorner[0])
                        + Math.signum(mostPositiveCorner[0]))
                * Math.abs(mostNegativeCorner[1] - mostPositiveCorner[1] * Math.signum(mostPositiveCorner[1])
                + Math.signum(mostPositiveCorner[1]))
                * Math.abs(mostNegativeCorner[2] - mostPositiveCorner[2] * Math.signum(mostPositiveCorner[2])
                + Math.signum(mostPositiveCorner[2]))
                + " blocks.");
    }

    // Reset the selection
    public boolean resetSelection() {
        double[] positionOneNew = {-1.0, -1.0, -1.0};
        double[] positionTwoNew = {-1.0, -1.0, -1.0};
        positionOne = positionOneNew;
        positionTwo = positionTwoNew;
        mostNegativeCorner = new double[3];
        mostPositiveCorner = new double[3];
        return true;
    }

    // Get a list of blocks contained by this selection
    public BlockIterator getBlocks(World world) {
        double[] pos1 = mostNegativeCorner;
        double[] pos2 = mostPositiveCorner;
        List<String> args = new ArrayList<>();
        args.add(Integer.toString((int) pos1[0]));
        args.add(Integer.toString((int) pos1[1]));
        args.add(Integer.toString((int) pos1[2]));
        args.add(Integer.toString((int) pos2[0]));
        args.add(Integer.toString((int) pos2[1]));
        args.add(Integer.toString((int) pos2[2]));
        args.add("1");
        return GlobalVars.iteratorManager.getIterator("cube").newIterator(args, world);
    }

    public Player getPlayer(UUID player){
        return Bukkit.getServer().getPlayer(player);
    }
}
