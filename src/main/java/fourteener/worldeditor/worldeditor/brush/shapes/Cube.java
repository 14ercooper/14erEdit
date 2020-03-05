package fourteener.worldeditor.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.worldeditor.brush.BrushShape;

public class Cube extends BrushShape {

	@Override
	public List<Block> GetBlocks(List<Double> args, double x, double y, double z) {
		List<Block> blockArray = new ArrayList<Block>();
		int cubeRad = (int) (args.get(0) / 2);
		for (int rx = -cubeRad; rx <= cubeRad; rx++) {
			for (int rz = -cubeRad; rz <= cubeRad; rz++) {
				for (int ry = -cubeRad; ry <= cubeRad; ry++) {
					blockArray.add(Main.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
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
