package com._14ercooper.worldeditor.functions.commands.logic;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class WaitCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	if (!function.isOperator) {
	    function.waitDelay = -1;
	}
    }
}
