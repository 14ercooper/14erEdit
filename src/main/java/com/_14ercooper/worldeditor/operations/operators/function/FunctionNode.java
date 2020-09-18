package com._14ercooper.worldeditor.operations.operators.function;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class FunctionNode extends Node {

    String filename;
    List<String> args = new ArrayList<String>();
    
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
	fx.run();
	return true;
    }

    @Override
    public int getArgCount() {
	return 2;
    }

}
