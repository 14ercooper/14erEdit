package com._14ercooper.worldeditor.main;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.block.CraftBlockEntityState;

import com._14ercooper.worldeditor.operations.Operator;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.v1_16_R1.BlockPosition;
import net.minecraft.server.v1_16_R1.MojangsonParser;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import net.minecraft.server.v1_16_R1.TileEntity;
import net.minecraft.server.v1_16_R1.World;

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
	
	public void setNBT(BlockState block, String nbt) {
		// Get the NBT Tag Compound
		NBTTagCompound tag = null;
		try {
			tag = MojangsonParser.parse(nbt);
		} catch (CommandSyntaxException e) {
			Main.logError("Unable to parse NBT", Operator.currentPlayer);
			return;
		}
		World w = ((CraftWorld) block.getLocation().getWorld()).getHandle();
		Main.logDebug(new BlockPosition(block.getX(), block.getY(), block.getZ()).getX() + " " + new BlockPosition(block.getX(), block.getY(), block.getZ()).getY() + " " + new BlockPosition(block.getX(), block.getY(), block.getZ()).getZ());
		TileEntity tileEntity = w.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
		tileEntity.load(w.getType(new BlockPosition(block.getX(), block.getY(), block.getZ())), tag);
	}
	
	public void setNBT(Block b, String nbt) {
		setNBT(b.getState(), nbt);
	}
}
