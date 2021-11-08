package com._14ercooper.worldeditor.functions.commands.variable;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class GetValCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
//	function.variables.set(Integer.parseInt(args.get(0).replaceAll("\\$v", "")), Double.parseDouble(function.templateArgs.get(Integer.parseInt(args.get(1).replaceAll("\\$", "")))));
        function.setVariable(args.get(0), Double.parseDouble(function.templateArgs.get(Integer.parseInt(args.get(1).replaceAll("\\$", "")))));
    }
}
