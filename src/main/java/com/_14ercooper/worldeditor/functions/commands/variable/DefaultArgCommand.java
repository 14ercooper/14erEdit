package com._14ercooper.worldeditor.functions.commands.variable;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class DefaultArgCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        if (function.templateArgs.size() < Integer.parseInt(args.get(0))) {
            function.templateArgs.add(args.get(1));
        }
    }
}
