package com._14ercooper.worldeditor.functions.commands.variable;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class RemoveCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	function.variableStack.remove(function.variables.size() - 1);
    }
}
