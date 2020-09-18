package com._14ercooper.worldeditor.functions.commands.logic;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class CompareTextCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	boolean isTrue = args.get(0).equalsIgnoreCase(args.get(1));
	if (isTrue)
	    function.cmpres = 1;
	else
	    function.cmpres = 0;
    }
}
