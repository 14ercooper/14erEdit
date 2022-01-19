// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.blockiterator;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockWrapper {
    public Block block;
    public int x, y, z;
    public List<String> otherArgs = new ArrayList<>();

    public BlockWrapper(Block block, int x, int y, int z) {
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
