package com._14ercooper.worldeditor.functions.commands.variable;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class GetCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	int offset = args.size() > 1 ? (int) function.parseVariable(args.get(1)) : 0;
	double val = function.variableStack.get(function.variableStack.size() - 1 - offset);
	function.setVariable(args.get(0), val);
    }
}
