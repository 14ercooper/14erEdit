package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

import java.util.ArrayList;
import java.util.List;

public class FunctionNode extends NumberNode {

    String filename;
    final List<String> args = new ArrayList<>();

    @Override
    public FunctionNode newNode() {
        FunctionNode node = new FunctionNode();
        node.filename = GlobalVars.operationParser.parseStringNode().getText();
        int argNum = (int) GlobalVars.operationParser.parseNumberNode().getValue();
        for (int i = 0; i < argNum; i++) {
            node.args.add(GlobalVars.operationParser.parseStringNode().getText());
        }
        return node;
    }

    @Override
    public boolean performNode() {
        Function fx = new Function(filename, args, Operator.currentPlayer, true);
        return Math.abs(fx.run()) > 0.001;
    }

    @Override
    public int getArgCount() {
        return 2;
    }

    @Override
    public double getValue() {
        return getValue(0);
    }

    @Override
    public double getValue(double center) {
        Function fx = new Function(filename, args, Operator.currentPlayer, true);
        //	Main.logDebug("Function return: " + val);
        return fx.run();
    }

    @Override
    public int getInt() {
        return getInt(0);
    }

    @Override
    public int getInt(int center) {
        Function fx = new Function(filename, args, Operator.currentPlayer, true);
        //	Main.logDebug("Function return: " + val);
        return (int) fx.run();
    }

}
