package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.DummyState;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class FunctionNode extends NumberNode {

    String filename;
    final List<String> args = new ArrayList<>();

    @Override
    public FunctionNode newNode(CommandSender currentPlayer) {
        FunctionNode node = new FunctionNode();
        node.filename = GlobalVars.operationParser.parseStringNode(currentPlayer).getText();
        int argNum = (int) GlobalVars.operationParser.parseNumberNode(currentPlayer).getValue(new DummyState(currentPlayer));
        for (int i = 0; i < argNum; i++) {
            node.args.add(GlobalVars.operationParser.parseStringNode(currentPlayer).getText());
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        Function fx = new Function(filename, args, state.getCurrentPlayer(), true, state);
        return Math.abs(fx.run()) > 0.001;
    }

    @Override
    public int getArgCount() {
        return 2;
    }

    @Override
    public double getValue(OperatorState state) {
        return getValue(state, 0);
    }

    @Override
    public double getValue(OperatorState state, double center) {
        Function fx = new Function(filename, args, state.getCurrentPlayer(), true, state);
        //	Main.logDebug("Function return: " + val);
        return fx.run();
    }

    @Override
    public int getInt(OperatorState state) {
        return getInt(state, 0);
    }

    @Override
    public int getInt(OperatorState state, int center) {
        Function fx = new Function(filename, args, state.getCurrentPlayer(), true, state);
        //	Main.logDebug("Function return: " + val);
        return (int) fx.run();
    }

}
