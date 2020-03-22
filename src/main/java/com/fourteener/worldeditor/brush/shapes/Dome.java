package com.fourteener.worldeditor.brush.shapes;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.GlobalVars;

public class Dome extends BrushShape {

	@Override
	public List<Block> GetBlocks(List<Double> args, double x, double y, double z) {
		List<Block> blockArray = new LinkedList<Block>();
		int dir = (int) (double) args.get(0);
		int radius = (int) (double) args.get(1);
		double radiusCorrection = args.get(2);
		int xs, xe, ys, ye, zs, ze;
		xs = xe = ys = ye = zs = ze = radius;
		switch (dir) {
		case 0:
			ys = 0;
			break;
		case 1:
			ye = 0;
			break;
		case 2:
			xs = 0;
			break;
		case 3:
			xe = 0;
			break;
		case 4:
			zs = 0;
			break;
		case 5:
			ze = 0;
			break;
		}
		for (int rx = -xs; rx <= xe; rx++) {
			for (int rz = -zs; rz <= ze; rz++) {
				for (int ry = -ys; ry <= ye; ry++) {
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
		return 3;
	}

}
