package com._14ercooper.worldeditor.functions.commands.variable;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class StoreCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        function.variableStack.add(function.parseVariable(args.get(0)));
    }
}
