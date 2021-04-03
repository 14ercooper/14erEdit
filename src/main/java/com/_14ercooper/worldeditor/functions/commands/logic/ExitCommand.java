package com._14ercooper.worldeditor.functions.commands.logic;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class ExitCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	function.exit = true;
	if (args.size() > 0) {
	    function.exitVal = Boolean.parseBoolean(args.get(0));
	}
    }
}
