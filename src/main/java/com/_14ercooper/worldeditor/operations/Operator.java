package com._14ercooper.worldeditor.operations;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.operators.core.EntryNode;
import com._14ercooper.worldeditor.operations.type.*;

public class Operator {
    public static Operator currentOperator;
    public static Block currentBlock;
    public static Player currentPlayer;
    public static boolean ignoringPhysics = false; // False to ignore physics, true to perform physics 'cause Minecraft
						   // is screwy
    public static Map<String, BlockVar> blockVars = new HashMap<String, BlockVar>();
    public static Map<String, ItemVar> itemVars = new HashMap<String, ItemVar>();
    public static Map<String, NumericVar> numericVars = new HashMap<String, NumericVar>();
    public static Map<String, MonsterVar> monsterVars = new HashMap<String, MonsterVar>();
    public static Map<String, SpawnerVar> spawnerVars = new HashMap<String, SpawnerVar>();
    public static Map<String, Operator> fileLoads = new HashMap<String, Operator>();

    public EntryNode entryNode;

    public boolean operateOnBlock(Block block, Player p) {
	try {
	    // Set global operator variables
	    currentOperator = this;
	    currentBlock = block;
	    currentPlayer = p;

	    // Perform the operation
	    return entryNode.performNode();
	}
	catch (Exception e) {
	    Main.logError("Could not perform operation. Please check your syntax.", p);
	    GlobalVars.asyncManager.dropAsync();
	    return false;
	}
    }

    public boolean operateOnBlock(Block block) {
	try {
	    // Set global operator variables
	    currentOperator = this;
	    currentBlock = block;
	    currentPlayer = null;

	    // Perform the operation
	    return entryNode.performNode();
	}
	catch (Exception e) {
	    Main.logError("Could not perform operation. Please check your syntax.", Bukkit.getConsoleSender());
	    GlobalVars.asyncManager.dropAsync();
	    return false;
	}
    }

    public boolean messyOperate() {
	try {
	    return entryNode.performNode();
	}
	catch (Exception e) {
	    Main.logError("Could not perform operation. Please check your syntax.", Bukkit.getConsoleSender());
	    GlobalVars.asyncManager.dropAsync();
	    return false;
	}
    }

    public Operator(String op, Player p) {
	currentPlayer = p; // Used to show errors in the parse process
	entryNode = GlobalVars.operationParser.parseOperation(op);
    }

    public Operator(EntryNode e, Player p) {
	currentPlayer = p;
	entryNode = e;
    }
}
