package com._14ercooper.worldeditor.operations.operators.variable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.SpawnerVar;

public class GetSpawnerCommandNode extends Node {

    String name;
    String path;

    @Override
    public GetSpawnerCommandNode newNode() {
        GetSpawnerCommandNode node = new GetSpawnerCommandNode();
        node.name = GlobalVars.operationParser.parseStringNode().contents;
        node.path = "plugins/14erEdit/Commands/" + GlobalVars.operationParser.parseStringNode();
        return node;
    }

    @Override
    public boolean performNode() {
        if (!Operator.spawnerVars.containsKey(name)) {
            Main.logError(
                    "Error performing get spawner command node. Please check your syntax (does the variable exist?).",
                    Operator.currentPlayer, null);
            return false;
        }
        SpawnerVar var = Operator.spawnerVars.get(name);
        String command = "setblock " + Operator.currentBlock.getX();
        command += " " + Operator.currentBlock.getY();
        command += " " + Operator.currentBlock.getZ();
        command += " minecraft:spawner";
        command += var.getNBT();
        command += " replace";
        Main.logDebug("Command: " + command);
        try {
            File f = new File(path.replace('/', File.separatorChar));
            if (!f.exists()) {
                f.getParentFile().mkdir();
                f.getParentFile().getParentFile().mkdir();
                f.createNewFile();
            } else {
                f.delete();
                f.getParentFile().mkdir();
                f.getParentFile().getParentFile().mkdir();
                f.createNewFile();
            }
            Files.write(Paths.get(path.replace('/', File.separatorChar)), command.getBytes());
        } catch (IOException e) {
            Main.logDebug("Could not open file");
        }
        return true;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
