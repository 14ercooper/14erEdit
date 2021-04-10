package com._14ercooper.worldeditor.operations.operators.variable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.ItemVar;
import com._14ercooper.worldeditor.operations.type.MonsterVar;
import com._14ercooper.worldeditor.operations.type.SpawnerVar;

public class LoadVariableNode extends Node {

    String type, name, path;

    @Override
    public LoadVariableNode newNode() {
        LoadVariableNode node = new LoadVariableNode();
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
                try {
                    ObjectInputStream oi = new ObjectInputStream(new FileInputStream(path));
                    Object obj = oi.readObject();
                    Operator.itemVars.put(name, (ItemVar) obj);
                    oi.close();
                    return true;
                } catch (IOException e) {
                    Main.logError("Could not load variable. Does the file exist?", Operator.currentPlayer, e);
                    Main.logDebug(e.getMessage());
                } catch (ClassNotFoundException e) {
                    Main.logError("Error loading varibale - class not found. Is the file a saved item?",
                            Operator.currentPlayer, e);
                    Main.logDebug(e.getMessage());
                }
            }
            if (type.equalsIgnoreCase("mob")) {
                try {
                    ObjectInputStream oi = new ObjectInputStream(new FileInputStream(path));
                    Object obj = oi.readObject();
                    Operator.monsterVars.put(name, (MonsterVar) obj);
                    oi.close();
                    return true;
                } catch (IOException e) {
                    Main.logError("Could not load variable. Does the file exist?", Operator.currentPlayer, e);
                    Main.logDebug(e.getMessage());
                } catch (ClassNotFoundException e) {
                    Main.logError("Error loading varibale - class not found. Is the file a saved monster?",
                            Operator.currentPlayer, e);
                    Main.logDebug(e.getMessage());
                }
            }
            if (type.equalsIgnoreCase("spwn")) {
                try {
                    ObjectInputStream oi = new ObjectInputStream(new FileInputStream(path));
                    Object obj = oi.readObject();
                    Operator.spawnerVars.put(name, (SpawnerVar) obj);
                    oi.close();
                    return true;
                } catch (IOException e) {
                    Main.logError("Could not load variable. Does the file exist?", Operator.currentPlayer, e);
                    Main.logDebug(e.getMessage());
                } catch (ClassNotFoundException e) {
                    Main.logError("Error loading varibale - class not found. Is the file a saved spawner?",
                            Operator.currentPlayer, e);
                    Main.logDebug(e.getMessage());
                }
            }
            Main.logError("Could not load variable. Only items, monsters, and spawners can be loaded.",
                    Operator.currentPlayer, null);
            return false;
        } catch (Exception e) {
            Main.logError("Error loading variable. Please check your syntax.", Operator.currentPlayer, e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
