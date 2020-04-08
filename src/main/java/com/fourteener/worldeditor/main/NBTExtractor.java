package com.fourteener.worldeditor.main;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlockEntityState;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class NBTExtractor {
	public String getNBT(BlockState bs) {
		if (!bs.getClass().getName().endsWith("CraftBlockState")) {
			@SuppressWarnings("rawtypes")
			CraftBlockEntityState cb = (CraftBlockEntityState) bs;
			NBTTagCompound ntc = cb.getSnapshotNBT();
			String nbt = ntc.asString();
			nbt = nbt.replaceAll(",?[xz]:[\\d-]+", "");
			nbt = nbt.replaceAll(",y:[\\d-]+", "");
			nbt = nbt.replaceAll(",?id:\"[A-Za-z:]+\"", "");
			nbt = nbt.replaceAll("\\{,", "{");
			return nbt;
		}
		else {
			return "";
		}
	}
	
	public String getNBT(Block b) {
		return getNBT(b.getState());
	}
}
