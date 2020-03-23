package com.fourteener.worldeditor.operations.operators.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.operations.Operator;

public class FacingNode extends BlockNode {
	
	BlockNode arg;
	
	public FacingNode newNode() {
		FacingNode node = new FacingNode();
		node.arg = (BlockNode) GlobalVars.operationParser.parsePart();
		return node;
	}
	
	public BlockData getData () {
		BlockData dat = arg.getData();
		Random rand = new Random();
		
		List<String> dirs = new ArrayList<String>();
		Block b = Operator.currentBlock.getBlock();
		// And next to a solid block
		if (b.getRelative(BlockFace.NORTH).getType() != Material.AIR) {
			dirs.add("[north=true]");
		}
		if (b.getRelative(BlockFace.EAST).getType() != Material.AIR) {
			dirs.add("[east=true]");
		}
		if (b.getRelative(BlockFace.SOUTH).getType() != Material.AIR) {
			dirs.add("[south=true]");
		}
		if (b.getRelative(BlockFace.WEST).getType() != Material.AIR) {
			dirs.add("[west=true]");
		}
		if (b.getRelative(BlockFace.UP).getType() != Material.AIR) {
			dirs.add("[up=true]");
		}
		if (b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
			dirs.add("[up=true]");
		}
		
		if (dirs.size() > 0) {
			String newData = arg.getBlock().toString().toLowerCase(Locale.ROOT);
			newData += dirs.get(rand.nextInt(dirs.size()));
			BlockData newDat = Bukkit.getServer().createBlockData(newData);
			dat = dat.merge(newDat);
		}
		
		return dat;
	}
	
	public int getArgCount () {
		return 1;
	}
}
