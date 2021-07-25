package com._14ercooper.worldeditor.blockiterator.iterators;

import com._14ercooper.schematics.SchemLite;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchemBrushIterator extends BlockIterator {

    SchemLite schem;
    BlockIterator schemIter;

    // Statics
//    public static String blockType = "";
//    public static String blockData = "";
//    public static String nbt = "";

    @Override
    public BlockIterator newIterator(List<String> arg, World world, CommandSender player) {
        try {
            List<String> args = new ArrayList<>();
            for (Object s : arg) {
                args.add((String) s);
            }
            SchemBrushIterator iter = new SchemBrushIterator();
            iter.iterWorld = world;
            int x = Integer.parseInt(args.get(0));
            int y = Integer.parseInt(args.get(1));
            int z = Integer.parseInt(args.get(2));
            iter.schem = new SchemLite(args.get(3), true, 0);
            iter.schem.openRead();
            iter.schemIter = iter.schem.getIterator(x - (iter.schem.getXSize() / 2), y - (iter.schem.getYSize() / 2),
                    z - (iter.schem.getZSize() / 2), world);
            return iter;
        } catch (Exception e) {
            Main.logError("Could not create schem brush iterator", player, e);
            return null;
        }
    }

    public void cleanup() {
        try {
            schem.closeRead();
        } catch (Exception e) {
            // This isn't a problem
        }
    }

    @Override
    public BlockWrapper getNextBlock(CommandSender player, boolean getBlock) {

        String blockType;
        String blockData;
        String nbt;

        // Update the schem block
        try {
            String[] data = schem.readNext();
            blockType = data[0];
            blockData = data[1];
            nbt = data[2];
        } catch (IOException e) {
            Main.logError("Could not read next block from schematic.", Bukkit.getConsoleSender(), e);
            blockType = blockData = nbt = "";
        }

        // Return the next world block
        BlockWrapper wrapper = schemIter.getNextBlock(player, getBlock);
        if (wrapper != null) {
            wrapper.otherArgs.add(blockType);
            wrapper.otherArgs.add(blockData);
            wrapper.otherArgs.add(nbt);
        }
        return wrapper;
    }

    @Override
    public long getTotalBlocks() {
        // How big is the schematic?
        return schemIter.getTotalBlocks();
    }

    @Override
    public long getRemainingBlocks() {
        // About how much longer to go?
        return schemIter.getRemainingBlocks();
    }

}
