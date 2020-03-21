package com.fourteener.worldeditor.brush.shapes;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.GlobalVars;

public class Voxel extends BrushShape {

	@Override
	public List<Block> GetBlocks(List<Double> args, double x, double y, double z) {
		List<Block> blockList = new LinkedList<Block>();
		blockList.add(GlobalVars.world.getBlockAt((int) x, (int) y, (int) z));
		return blockList;
	}

	@Override
	public double GetArgCount() {
		return 0;
	}

}
