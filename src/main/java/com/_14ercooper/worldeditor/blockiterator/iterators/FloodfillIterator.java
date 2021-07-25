package com._14ercooper.worldeditor.blockiterator.iterators;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.*;

public class FloodfillIterator extends BlockIterator {

    Deque<BlockObject> blockObjects = new ArrayDeque<>();
    Set<BlockObject> seenBlocks = new HashSet<>();
    int maxDepth;
    long totalBlocks;
    long doneBlocks = 0;
    boolean firstBlock = true;

    @Override
    public BlockIterator newIterator(List<String> args, World world, CommandSender player) {
        FloodfillIterator iter = new FloodfillIterator();

        int x_ = Integer.parseInt(args.get(0));
        int y_ = Integer.parseInt(args.get(1));
        int z_ = Integer.parseInt(args.get(2));
        BlockObject blkObj = new BlockObject(x_, y_, z_, 0);
        iter.blockObjects.add(blkObj);
        iter.seenBlocks.add(blkObj);

        iter.maxDepth = Integer.parseInt(args.get(3));

        iter.totalBlocks = (long) (1.3333 * 3.1415 * (2L * iter.maxDepth + 2));

        return iter;
    }

    @Override
    public BlockWrapper getNextBlock(CommandSender player, boolean getBlock) {
        // If no next block, we're done
        if (blockObjects.size() == 0) {
            return null;
        }

        // Get the next block from the list and the condition operator
        BlockObject blkObject = blockObjects.removeFirst();
        Operator conditionOperator = (Operator) objectArgs.get("FloodfillCondition");
        OperatorState operatorTempState = (OperatorState) objectArgs.get("OperatorState");

        // Handle first block (make sure it meets the conditions
        if (firstBlock) {
            operatorTempState.setCurrentBlock(blkObject.getBlock(operatorTempState.getCurrentWorld()));
            boolean result = conditionOperator.operateOnBlock(operatorTempState);
            if (!result) {
                return null;
            }
            doneBlocks++;
            firstBlock = false;
        }

        // Process adjacent blocks
        List<BlockObject> adjacents = new ArrayList<>();
        adjacents.add(new BlockObject(blkObject.x + 1, blkObject.y, blkObject.z, blkObject.depth + 1));
        adjacents.add(new BlockObject(blkObject.x, blkObject.y + 1, blkObject.z, blkObject.depth + 1));
        adjacents.add(new BlockObject(blkObject.x, blkObject.y, blkObject.z + 1, blkObject.depth + 1));
        adjacents.add(new BlockObject(blkObject.x - 1, blkObject.y, blkObject.z, blkObject.depth + 1));
        adjacents.add(new BlockObject(blkObject.x, blkObject.y - 1, blkObject.z, blkObject.depth + 1));
        adjacents.add(new BlockObject(blkObject.x, blkObject.y, blkObject.z - 1, blkObject.depth + 1));
        for (BlockObject b : adjacents) {
            if (!seenBlocks.contains(b)) {
                seenBlocks.add(b);
                if (b.depth > maxDepth) {
                    continue;
                }
                operatorTempState.setCurrentBlock(b.getBlock(operatorTempState.getCurrentWorld()));
                boolean result = conditionOperator.operateOnBlock(operatorTempState);
                if (result) {
                    blockObjects.add(b);
                }
            }
        }

        // Return a new block
        if (getBlock) {
            return new BlockWrapper(blkObject.getBlock(operatorTempState.getCurrentWorld()), blkObject.x, blkObject.y, blkObject.z);
        }
        else {
            return new BlockWrapper(null, blkObject.x, blkObject.y, blkObject.z);
        }
    }

    @Override
    public long getTotalBlocks() {
        return totalBlocks;
    }

    @Override
    public long getRemainingBlocks() {
        return totalBlocks - doneBlocks;
    }

    private static class BlockObject {
        public final int x, y, z;
        public final int depth;

        private BlockObject(int x, int y, int z, int depth) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.depth = depth;
        }

        @Override
        public boolean equals(Object b) {
            if (!(b instanceof BlockObject)) {
                return false;
            }
            return this.x == ((BlockObject) b).x && this.y == ((BlockObject) b).y && this.z == ((BlockObject) b).z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        public Block getBlock(World world) {
            return world.getBlockAt(x, y, z);
        }
    }
}
