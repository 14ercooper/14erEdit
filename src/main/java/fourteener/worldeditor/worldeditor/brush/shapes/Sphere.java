package fourteener.worldeditor.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.worldeditor.brush.BrushShape;

public class Sphere extends BrushShape {

	@Override
	public List<Block> GetBlocks(List<Double> args, double x, double y, double z) {
		List<Block> blockArray = new ArrayList<Block>();
		int radius = (int) (double) args.get(0);
		double radiusCorrection = args.get(1);
		for (int rx = -radius; rx <= radius; rx++) {
			for (int rz = -radius; rz <= radius; rz++) {
				for (int ry = -radius; ry <= radius; ry++) {
					if (rx*rx + ry*ry + rz*rz <= (radius + radiusCorrection)*(radius + radiusCorrection)) {
						blockArray.add(Main.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
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
