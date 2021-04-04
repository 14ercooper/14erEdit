package com._14ercooper.worldeditor.functions.commands.logic;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class ExitCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	function.exit = true;
	if (args.size() > 0) {
	    try {
		if (Boolean.parseBoolean(args.get(0)))
		    function.exitVal = 1;
		else function.exitVal = 0;
	    }
	    catch (Exception e) {
		try {
		    function.exitVal = function.parseVariable(args.get(0));
		}
		catch (Exception e2) {
		    function.exitVal = Double.parseDouble(args.get(0));
		}
	    }
	}
    }
}
