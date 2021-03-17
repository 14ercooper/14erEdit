package com._14ercooper.worldeditor.main;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlockEntityState;
import net.minecraft.server.v1_16_R3.NBTTagCompound;

public class NBTExtractor {
    public String getNBT(BlockState bs) {
	if (!bs.getClass().getName().endsWith("CraftBlockState")) {
	    @SuppressWarnings("rawtypes")
	    CraftBlockEntityState cb = (CraftBlockEntityState) bs;
	    NBTTagCompound ntc = cb.getSnapshotNBT();
	    String nbt = ntc.asString();
	    return nbt;
	}
	else {
	    return "";
	}
//	return "";
    }

    public String getNBT(Block b) {
	return getNBT(b.getState());
    }
//	
//	public void setNBT(BlockState block, String nbt) {
//		// Get the NBT Tag Compound
//		NBTTagCompound tag = null;
//		try {
//			tag = MojangsonParser.parse(nbt);
//		} catch (CommandSyntaxException e) {
//			Main.logError("Unable to parse NBT", Operator.currentPlayer);
//			return;
//		}
//		World w = ((CraftWorld) block.getLocation().getWorld()).getHandle();
//		Main.logDebug(new BlockPosition(block.getX(), block.getY(), block.getZ()).getX() + " " + new BlockPosition(block.getX(), block.getY(), block.getZ()).getY() + " " + new BlockPosition(block.getX(), block.getY(), block.getZ()).getZ());
//		TileEntity tileEntity = w.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
//		tileEntity.load(w.getType(new BlockPosition(block.getX(), block.getY(), block.getZ())), tag);
//	}
//	
//	public void setNBT(Block b, String nbt) {
//		setNBT(b.getState(), nbt);
//	}
}
