package com.fourteener.worldeditor.brush.shapes;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.*;

public class Cube extends BrushShape {

	@Override
	public List<Block> GetBlocks(List<Double> args, double x, double y, double z) {
		List<Block> blockArray = new LinkedList<Block>();
		int cubeRad = (int) (args.get(0) / 2);
		for (int rx = -cubeRad; rx <= cubeRad; rx++) {
			for (int rz = -cubeRad; rz <= cubeRad; rz++) {
				for (int ry = -cubeRad; ry <= cubeRad; ry++) {
					blockArray.add(GlobalVars.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
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
