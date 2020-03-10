package com.fourteener.worldeditor.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.worldeditor.brush.BrushShape;

public class Diamond extends BrushShape {

	@Override
	public List<Block> GetBlocks(List<Double> args, double x, double y, double z) {
		// This uses the Manhattan distance
		List<Block> blockArray = new ArrayList<Block>();
		int radius = (int) (double) args.get(0);
		for (int rx = -radius; rx <= radius; rx++) {
			for (int rz = -radius; rz <= radius; rz++) {
				for (int ry = -radius; ry <= radius; ry++) {
					if (Math.abs(rx) + Math.abs(ry) + Math.abs(rz) <= radius) {
						blockArray.add(GlobalVars.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
					}
				}
			}
		}
		return blockArray;
	}

	@Override
	public double GetArgCount() {
		return 1;
	}

}
