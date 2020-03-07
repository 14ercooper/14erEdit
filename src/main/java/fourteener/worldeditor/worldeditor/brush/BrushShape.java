package fourteener.worldeditor.worldeditor.brush;

import java.util.List;

import org.bukkit.block.Block;

public abstract class BrushShape {
	public abstract List<Block> GetBlocks (List<Double> args, double x, double y, double z);
	
	public abstract double GetArgCount();
}
