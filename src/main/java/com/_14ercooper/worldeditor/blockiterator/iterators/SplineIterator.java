// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.blockiterator.iterators;

import com._14ercooper.math.Point3;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SplineIterator extends BlockIterator {

    double alpha;
    List<Point3> points;
    Point3 prePoint, postPoint;
    int segments;
    int currentSegment, currentBlock;

    boolean hasSetSegmentTValues = false;
    double t0 = 0, t1 = 0, t2 = 0, t3 = 0;
    double diff32 = 0, diff21 = 0, diff10 = 0, diff31 = 0, diff20 = 0;

    private static final long NUM_PER_SEGMENT = 10000L;

    @Override
    public SplineIterator newIterator(List<String> args, World world, CommandSender player) {
        try {
            SplineIterator iterator = new SplineIterator();
            iterator.iterWorld = world;
            if (args.size() < 3) {
                throw new Exception("Not enough iterator arguments");
            }
            iterator.alpha = Double.parseDouble(args.get(0));
            iterator.points = new ArrayList<>();
            Point3 gridAlign = new Point3(0.25, 0.25, 0.25);
            for (int i = 1; i < args.size(); i++) {
                iterator.points.add(Point3.fromString(args.get(i)).add(gridAlign));
            }
            Point3 ba = iterator.points.get(1).sub(iterator.points.get(0));
            Point3 multNeg = ba.mult(-1);
            iterator.prePoint = multNeg.add(iterator.points.get(0));
            ba = iterator.points.get(iterator.points.size() - 2).sub(iterator.points.get(iterator.points.size() - 1));
            multNeg = ba.mult(-1);
            iterator.postPoint = multNeg.add(iterator.points.get(iterator.points.size() - 1));
            iterator.segments = iterator.points.size() - 1;
            iterator.currentSegment = 0;
            iterator.currentBlock = 0;

            return iterator;
        }
        catch (Exception e) {
            Main.logError("Error creating spline iterator. Please check that you provided enough selection points", player, e);
            return null;
        }
    }

    @Override
    public BlockWrapper getNextBlock(CommandSender player, boolean getBlock) {
        long splineX;
        long splineY;
        long splineZ;

        currentBlock++;
        if (currentBlock >= NUM_PER_SEGMENT) {
            currentBlock = 0;
            currentSegment++;
            hasSetSegmentTValues = false;
        }
        if (currentSegment >= segments) {
            return null;
        }

        if (!hasSetSegmentTValues) {
            hasSetSegmentTValues = true;
            t1 = Math.pow(Math.sqrt(
                    Math.pow(diffX(currentSegment, 1), 2) +
                    Math.pow(diffY(currentSegment, 1), 2) +
                    Math.pow(diffZ(currentSegment, 1), 2)), alpha) + t0;
            t2 = Math.pow(Math.sqrt(
                    Math.pow(diffX(currentSegment, 2), 2) +
                    Math.pow(diffY(currentSegment, 2), 2) +
                    Math.pow(diffZ(currentSegment, 2), 2)), alpha) + t1;
            t3 = Math.pow(Math.sqrt(Math.pow(
                    diffX(currentSegment, 3), 2) +
                    Math.pow(diffY(currentSegment, 3), 2) +
                    Math.pow(diffZ(currentSegment, 3), 2)), alpha) + t2;

            diff32 = 1.0 / (t3 - t2);
            diff21 = 1.0 / (t2 - t1);
            diff10 = 1.0 / (t1 - t0);
            diff31 = 1.0 / (t3 - t1);
            diff20 = 1.0 / (t2 - t0);
        }

        double tPercent = currentBlock / ((double) NUM_PER_SEGMENT);
        double t = t1 + ((t2 - t1) * tPercent);

        Point3 p0 = getSegmentPoint(currentSegment, 0);
        Point3 p1 = getSegmentPoint(currentSegment, 1);
        Point3 p2 = getSegmentPoint(currentSegment, 2);
        Point3 p3 = getSegmentPoint(currentSegment, 3);

        Point3 a1 = p0.mult((t1 - t) * diff10).add(p1.mult((t - t0) * diff10));
        Point3 a2 = p1.mult((t2 - t) * diff21).add(p2.mult((t - t1) * diff21));
        Point3 a3 = p2.mult((t3 - t) * diff32).add(p3.mult((t - t2) * diff32));
        Point3 b1 = a1.mult((t2 - t) * diff20).add(a2.mult((t - t0) * diff20));
        Point3 b2 = a2.mult((t3 - t) * diff31).add(a3.mult((t - t1) * diff31));
        Point3 c = b1.mult((t2 - t) * diff21).add(b2.mult((t - t1) * diff21));

        splineX = Math.round(c.getX());
        splineY = Math.round(c.getY());
        splineZ = Math.round(c.getZ());

        if (getBlock) {
            return new BlockWrapper(iterWorld.getBlockAt((int) splineX, (int) splineY, (int) splineZ), (int) splineX, (int) splineY, (int) splineZ);
        } else {
            return new BlockWrapper(null, (int) splineX, (int) splineY, (int) splineZ);
        }
    }

    @Override
    public long getTotalBlocks() {
        return segments * NUM_PER_SEGMENT;
    }

    @Override
    public long getRemainingBlocks() {
        return getTotalBlocks() - doneBlocks;
    }

    private double diffX(int segment, int tIndex) {
        Point3 thisPoint = getSegmentPoint(segment, tIndex);
        Point3 lastPoint = getSegmentPoint(segment, tIndex - 1);
        return thisPoint.getX() - lastPoint.getX();
    }

    private double diffY(int segment, int tIndex) {
        Point3 thisPoint = getSegmentPoint(segment, tIndex);
        Point3 lastPoint = getSegmentPoint(segment, tIndex - 1);
        return thisPoint.getY() - lastPoint.getY();
    }

    private double diffZ(int segment, int tIndex) {
        Point3 thisPoint = getSegmentPoint(segment, tIndex);
        Point3 lastPoint = getSegmentPoint(segment, tIndex - 1);
        return thisPoint.getZ() - lastPoint.getZ();
    }

    private Point3 getSegmentPoint(int segment, int index) {
        return getPointIndex(segment + index - 1);
    }

    private Point3 getPointIndex(int index) {
        if (index < 0) {
            return prePoint;
        }
        if (index >= points.size()) {
            return postPoint;
        }
        return points.get(index);
    }

}
