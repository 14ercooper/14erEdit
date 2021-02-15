package com._14ercooper.worldeditor.functions.commands.math;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;
import com._14ercooper.worldeditor.main.Main;

public class RandCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	int min = (int) function.parseVariable(args.get(1));
	int max = (int) function.parseVariable(args.get(2));
//	function.setVariable(args.get(0), rand.nextInt(max - min) + min);
	function.setVariable(args.get(0), Main.randRange(min, max));
	
    }
}
