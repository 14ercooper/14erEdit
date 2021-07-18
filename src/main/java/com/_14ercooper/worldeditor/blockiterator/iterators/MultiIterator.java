package com._14ercooper.worldeditor.blockiterator.iterators;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MultiIterator extends BlockIterator {

    public final List<BlockIterator> childIterators = new ArrayList<>();

    @Override
    public BlockIterator newIterator(List<String> args, World world, CommandSender player) {
        Main.logError("MultiIterator does not support the standard constructor", player, null);
        return null;
    }

    public BlockIterator newIterator(Set<BlockIterator> children) {
        MultiIterator iter = new MultiIterator();
        iter.childIterators.addAll(children);
        return iter;
    }

    @Override
    public Block getNextBlock() {
        Block next = null;
        while (next == null) {
            if (childIterators.isEmpty())
                return null;
            next = childIterators.get(0).getNextBlock();
            if (next == null) {
                if (childIterators.isEmpty())
                    return null;
                childIterators.remove(0);
            }
        }
        return next;
    }

    @Override
    public long getTotalBlocks() {
        long total = 0;
        for (BlockIterator b : childIterators) {
            total += b.getTotalBlocks();
        }
        return total;
    }

    @Override
    public long getRemainingBlocks() {
        long total = 0;
        for (BlockIterator b : childIterators) {
            total += b.getRemainingBlocks();
        }
        return total;
    }

}
