package fourteener.worldeditor.operations;

import org.bukkit.block.Block;

public class Operator {
	public boolean operateOnBlock (Block block) {
		return false;
	}
	
	public static Operator newOperator (String op) {
		return null;
	}
}
