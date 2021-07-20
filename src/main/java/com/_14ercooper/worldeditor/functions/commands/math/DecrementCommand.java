package com._14ercooper.worldeditor.functions.commands.math;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class DecrementCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        function.setVariable(args.get(0), function.parseVariable(args.get(0)) - 1);
    }
}
