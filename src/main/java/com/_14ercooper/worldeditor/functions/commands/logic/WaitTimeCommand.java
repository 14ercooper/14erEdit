package com._14ercooper.worldeditor.functions.commands.logic;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class WaitTimeCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        if (!function.isOperator) {
            function.waitDelay = Integer.parseInt(args.get(0));
        }
    }
}
