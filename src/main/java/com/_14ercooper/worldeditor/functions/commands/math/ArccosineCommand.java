package com._14ercooper.worldeditor.functions.commands.math;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class ArccosineCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        double num1 = function.parseVariable(args.get(0));
        double result = Math.acos(Math.toRadians(num1));
        function.setVariable(args.get(1), result);
    }
}
