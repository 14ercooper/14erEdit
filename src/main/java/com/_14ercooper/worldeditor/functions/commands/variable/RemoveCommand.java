package com._14ercooper.worldeditor.functions.commands.variable;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class RemoveCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        function.variableStack.remove(function.variableStack.size() - 1);
    }
}
