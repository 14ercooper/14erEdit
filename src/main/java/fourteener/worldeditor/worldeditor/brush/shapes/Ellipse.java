package fourteener.worldeditor.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.worldeditor.brush.BrushShape;

public class Ellipse extends BrushShape {

	@Override
	public List<Block> GetBlocks(List<Double> args, double x, double y, double z) {
		// Generate the ellipse
		List<Block> blockArray = new ArrayList<Block>();
		for (double rx = -args.get(0); rx <= args.get(0); rx++) {
			for (double ry = -args.get(1); ry <= args.get(1); ry++) {
				for (double rz = -args.get(2); rz <= args.get(2); rz++) {
					if ((((rx * rx) / (args.get(0) * args.get(0))) + ((ry * ry) / (args.get(1) * args.get(1))) + ((rz * rz) / (args.get(2) * args.get(2)))) <= (1 + args.get(3))) {
						blockArray.add(Main.world.getBlockAt((int) (x + rx), (int) (y + ry), (int) (z + rz)));
					}
				}
			}
		}
		return blockArray;
	}

	@Override
	public double GetArgCount() {
		return 4;
	}

}
