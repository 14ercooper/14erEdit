package fourteener.worldeditor.operations;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import fourteener.worldeditor.main.GlobalVars;
import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.operators.core.EntryNode;
import fourteener.worldeditor.operations.type.*;

public class Operator {
	public static Operator currentOperator;
	public static BlockState currentBlock;
	public static Player currentPlayer;
	public static boolean ignoringPhysics = false; // False to ignore physics, true to perform physics 'cause Minecraft is screwy
	public static Map<String, BlockVar> blockVars = new HashMap<String, BlockVar>();
	public static Map<String, ItemVar> itemVars = new HashMap<String, ItemVar>();
	public static Map<String, NumericVar> numericVars = new HashMap<String, NumericVar>();
	public static Map<String, MonsterVar> monsterVars = new HashMap<String, MonsterVar>();
	public static Map<String, SpawnerVar> spawnerVars = new HashMap<String, SpawnerVar>();
	public static Map<String, Operator> fileLoads = new HashMap<String, Operator>();
	
	public EntryNode entryNode;
	
	public boolean operateOnBlock (BlockState block, Player p) {
		// Set global operator variables
		currentOperator = this;
		currentBlock = block;
		currentPlayer = p;
		
		// Perform the operation
		return entryNode.performNode();
	}
	
	public boolean messyOperate () {
		return entryNode.performNode();
	}
	
	public static Operator newOperator (String op) {
		Operator operator = new Operator();
		operator.entryNode = GlobalVars.operationParser.parseOperation(op);
		if (operator.entryNode == null) {
			return null;
		}
		Main.logDebug("Operator being returned");
		return operator;
	}
}
