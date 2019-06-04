package fourteener.worldeditor.operations.operators;

import org.bukkit.Location;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;

// This node currently accepts both 2 and 3 as dimensions using worldspace
// 4 dimensions uses worldspace plus average of the three other dimensions, creating good looking results
public class SimplexNode extends Node {
	
	public NumberNode arg1, arg2;
	
	public static SimplexNode newNode (NumberNode dimensions, NumberNode cutoff) {
		SimplexNode simplexNode = new SimplexNode();
		simplexNode.arg1 = dimensions;
		simplexNode.arg2 = cutoff;
		return simplexNode;
	}
	
	public boolean performNode () {
		// The range on all of these are useful for double inaccuracy
		if (arg1.getValue() <= 2.1 && arg1.getValue() >= 1.9) {
			Location loc = Operator.currentBlock.getLocation();
			return Main.simplexNoise.noise(loc.getX(), loc.getZ()) <= arg2.getValue();
		}
		if (arg1.getValue() <= 3.1 && arg1.getValue() >= 2.9) {
			Location loc = Operator.currentBlock.getLocation();
			return Main.simplexNoise.noise(loc.getX(), loc.getY(), loc.getZ()) <= arg2.getValue();
		}
		if (arg1.getValue() <= 4.1 && arg1.getValue() >= 3.9) {
			Location loc = Operator.currentBlock.getLocation();
			return Main.simplexNoise.noise(loc.getX(), loc.getY(), loc.getZ(), (loc.getX() + loc.getY() + loc.getZ()) * 0.33333333) <= arg2.getValue();
		}
		return false;
	}
	
	public static int getArgCount () {
		return 2;
	}
}
