package com._14ercooper.worldeditor.operations.operators.variable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.ItemVar;
import com._14ercooper.worldeditor.operations.type.MonsterVar;
import com._14ercooper.worldeditor.operations.type.SpawnerVar;

public class SaveVariableNode extends Node {

    String type, name, path;

    @Override
    public SaveVariableNode newNode() {
        SaveVariableNode node = new SaveVariableNode();
        node.type = GlobalVars.operationParser.parseStringNode().contents;
        node.name = GlobalVars.operationParser.parseStringNode().contents;
        node.path = "plugins/14erEdit/vars/"
                + GlobalVars.operationParser.parseStringNode().contents.replace('/', File.pathSeparatorChar);
        return node;
    }

    @Override
    public boolean performNode() {
        try {
            File f = new File(path);
            f.getParentFile().mkdirs();
            if (type.equalsIgnoreCase("itm")) {
                ItemVar var = Operator.itemVars.get(name);
                try {
                    Files.deleteIfExists(f.toPath());
                    ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(path));
                    oo.writeObject(var);
                    oo.close();
                    return true;
                } catch (IOException e) {
                    Main.logError("Could not open file to save variable.", Operator.currentPlayer, e);
                    Main.logDebug(e.getMessage());
                }
            }
            if (type.equalsIgnoreCase("mob")) {
                MonsterVar var = Operator.monsterVars.get(name);
                try {
                    Files.deleteIfExists(f.toPath());
                    ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(path));
                    oo.writeObject(var);
                    oo.close();
                    return true;
                } catch (IOException e) {
                    Main.logError("Could not open file to save variable.", Operator.currentPlayer, e);
                    Main.logDebug(e.getMessage());
                }
            }
            if (type.equalsIgnoreCase("spwn")) {
                SpawnerVar var = Operator.spawnerVars.get(name);
                try {
                    Files.deleteIfExists(f.toPath());
                    ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(path));
                    oo.writeObject(var);
                    oo.close();
                    return true;
                } catch (IOException e) {
                    Main.logError("Could not open file to save variable.", Operator.currentPlayer, e);
                    Main.logDebug(e.getMessage());
                }
            }
            Main.logError("Could not save variable to disk. Only items, monsters, and spawners may be saved.",
                    Operator.currentPlayer, null);
            return false;
        } catch (Exception e) {
            Main.logError("Error saving variable to disk. Please check your syntax.", Operator.currentPlayer, e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
