package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.SpawnerVar;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;

public class SetSpawnerNode extends Node {

    String name;

    @Override
    public SetSpawnerNode newNode() {
        SetSpawnerNode node = new SetSpawnerNode();
        node.name = GlobalVars.operationParser.parseStringNode().contents;
        return node;
    }

    @Override
    public boolean performNode() {
        if (!Operator.spawnerVars.containsKey(name)) {
            Main.logError("Error performing set spawner node. Please check your syntax (does the variable exist?).",
                    Operator.currentPlayer, null);
            return false;
        }
        SpawnerVar var = Operator.spawnerVars.get(name);
        BlockState oldBS = Operator.currentBlock.getState();
        String command = "setblock " + Operator.currentBlock.getX();
        command += " " + Operator.currentBlock.getY();
        command += " " + Operator.currentBlock.getZ();
        command += " minecraft:spawner";
        command += var.getNBT();
        command += " replace";
        Main.logDebug("Command: " + command);
        Bukkit.getServer().dispatchCommand(Operator.currentPlayer, command);
        GlobalVars.asyncManager.currentAsyncOp.getUndo().addBlock(oldBS, Operator.currentBlock.getState());
        return true;
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
