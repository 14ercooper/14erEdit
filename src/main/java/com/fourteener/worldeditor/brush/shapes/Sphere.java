package com.fourteener.worldeditor.brush.shapes;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.*;

public class Sphere extends BrushShape {

	@Override
	public List<Block> GetBlocks(List<Double> args, double x, double y, double z) {
		List<Block> blockArray = new LinkedList<Block>();
		int radius = (int) (double) args.get(0);
		double radiusCorrection = args.get(1);
		for (int rx = -radius; rx <= radius; rx++) {
			for (int rz = -radius; rz <= radius; rz++) {
				for (int ry = -radius; ry <= radius; ry++) {
					if (rx*rx + ry*ry + rz*rz <= (radius + radiusCorrection)*(radius + radiusCorrection)) {
						blockArray.add(GlobalVars.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
					}
				}
			}
		}
		return blockArray;
	}

	@Override
	public double GetArgCount() {
		return 2;
	}
	
}
