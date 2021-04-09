package com._14ercooper.worldeditor.operations;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.operators.core.EntryNode;
import com._14ercooper.worldeditor.operations.type.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Operator {
    public static Operator currentOperator;
    public static Block currentBlock;
    public static Player currentPlayer;
    private static Player firstPlayer = null;
    public static boolean ignoringPhysics = false; // False to ignore physics, true to perform physics 'cause Minecraft
    public static boolean inSetNode = false;
    // is screwy
    public static final Map<String, BlockVar> blockVars = new HashMap<>();
    public static final Map<String, ItemVar> itemVars = new HashMap<>();
    public static final Map<String, NumericVar> numericVars = new HashMap<>();
    public static final Map<String, MonsterVar> monsterVars = new HashMap<>();
    public static final Map<String, SpawnerVar> spawnerVars = new HashMap<>();
    public static final Map<String, Operator> fileLoads = new HashMap<>();

    public final EntryNode entryNode;

    public void operateOnBlock(Block block, Player p) {
        try {
            // Set global operator variables
            currentOperator = this;
            currentBlock = block;
            currentPlayer = p;
            if (currentPlayer == null) {
                if (firstPlayer == null) {
                    firstPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[0];
                }
                currentPlayer = firstPlayer;
            }

            // Perform the operation
            entryNode.performNode();
        }
	catch (Exception e) {
	    Main.logError("Could not perform operation. Please check your syntax.", p, e);
	    GlobalVars.asyncManager.dropAsync();
	}
    }

    public void operateOnBlock(Block block) {
        try {
            // Set global operator variables
            currentOperator = this;
            currentBlock = block;
            currentPlayer = null;
            if (currentPlayer == null) {
                if (firstPlayer == null) {
                    firstPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[0];
                }
                currentPlayer = firstPlayer;
            }

            // Perform the operation
            entryNode.performNode();
        }
	catch (Exception e) {
	    Main.logError("Could not perform operation. Please check your syntax.", Bukkit.getConsoleSender(), e);
	    GlobalVars.asyncManager.dropAsync();
	}
    }

    public boolean messyOperate() {
	try {
	    return entryNode.performNode();
	}
	catch (Exception e) {
	    Main.logError("Could not perform operation. Please check your syntax.", Bukkit.getConsoleSender(), e);
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
