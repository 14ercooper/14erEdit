package fourteener.worldeditor.operations;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import fourteener.worldeditor.operations.operators.EntryNode;

public class Operator {
	public static Operator currentOperator;
	public static BlockState currentBlock;
	public static Player currentPlayer;
	public static boolean ignoringPhysics = false; // False to ignore physics, true to perform physics 'cause Minecraft is screwy
	
	public EntryNode entryNode;
	
	public boolean operateOnBlock (BlockState block, Player p) {
		// Set global operator variables
		currentOperator = this;
		currentBlock = block;
		currentPlayer = p;
		
		// Perform the operation
		return entryNode.performNode();
	}
	
	public static Operator newOperator (String op) {
		Operator operator = new Operator();
		operator.entryNode = Parser.parseOperation(op);
		if (operator.entryNode == null) {
			return null;
		}
		Bukkit.getServer().broadcastMessage("§c[DEBUG] Operator being returned"); // -----
		return operator;
	}
}
