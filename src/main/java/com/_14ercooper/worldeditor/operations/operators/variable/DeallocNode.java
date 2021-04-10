package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class DeallocNode extends Node {

    String type, name;

    @Override
    public DeallocNode newNode() {
        DeallocNode node = new DeallocNode();
        node.type = GlobalVars.operationParser.parseStringNode().contents;
        node.name = GlobalVars.operationParser.parseStringNode().contents;
        return node;
    }

    @Override
    public boolean performNode() {
        if (type.equalsIgnoreCase("num")) {
            Operator.numericVars.remove(name);
            return true;
        }
        if (type.equalsIgnoreCase("itm")) {
            Operator.itemVars.remove(name);
            return true;
        }
        if (type.equalsIgnoreCase("blk")) {
            Operator.blockVars.remove(name);
            return true;
        }
        if (type.equalsIgnoreCase("mob")) {
            Operator.monsterVars.remove(name);
            return true;
        }
        if (type.equalsIgnoreCase("spwn")) {
            Operator.spawnerVars.remove(name);
            return true;
        }
        if (type.equalsIgnoreCase("file")) {
            Operator.fileLoads.remove(name);
            return true;
        }
        Main.logError("Could not deallocate variable, type \"" + type + "\" not found.", Operator.currentPlayer, null);
        return false;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
