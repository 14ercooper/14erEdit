package com._14ercooper.worldeditor.functions.commands.math;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class LogCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        double num1 = function.parseVariable(args.get(0));
        double num2 = function.parseVariable(args.get(1));
        double result = Math.log(num1) / Math.log(num2);
        function.setVariable(args.get(2), result);
    }
}