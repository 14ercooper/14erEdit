package com._14ercooper.worldeditor.main;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlockEntityState;

public class NBTExtractor {
    public String getNBT(BlockState bs) {
        if (!bs.getClass().getName().endsWith("CraftBlockState")) {
            @SuppressWarnings("rawtypes")
            CraftBlockEntityState cb = (CraftBlockEntityState) bs;
            NBTTagCompound ntc = cb.getSnapshotNBT();
            return ntc.asString();
        } else {
            return "";
        }
//	return "";
    }

    public String getNBT(Block b) {
        return getNBT(b.getState());
    }
}
