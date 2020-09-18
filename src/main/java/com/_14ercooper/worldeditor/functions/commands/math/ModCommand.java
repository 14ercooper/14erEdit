package com._14ercooper.worldeditor.functions.commands.math;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class ModCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	double num1 = function.parseVariable(args.get(0));
	double num2 = function.parseVariable(args.get(1));
	double result = (int) num1 % (int) num2;
	function.setVariable(args.get(2), result);
    }
}
