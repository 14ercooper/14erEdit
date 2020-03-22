package com.fourteener.worldeditor.main;

import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlockEntityState;

import com.fourteener.worldeditor.operations.Operator;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class NBTExtractor {
	public String getNBT(BlockState bs) {
		Main.logDebug(Operator.currentBlock.getClass().getName());
		if (!bs.getClass().getName().endsWith("CraftBlockState")) {
			@SuppressWarnings("rawtypes")
			CraftBlockEntityState cb = (CraftBlockEntityState) bs;
			NBTTagCompound ntc = cb.getSnapshotNBT();
			return ntc.asString();
		}
		else {
			return "";
		}
	}
}
