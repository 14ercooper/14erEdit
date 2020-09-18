package com._14ercooper.worldeditor.functions.commands.math;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class CosineCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	double num1 = function.parseVariable(args.get(0));
	double result = Math.cos(Math.toRadians(num1));
	function.setVariable(args.get(1), result);
    }
}
