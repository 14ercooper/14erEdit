// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.functions.commands.logic;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class CompareVariableCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        boolean isEqual = Math.abs(function.parseVariable(args.get(0)) - function.parseVariable(args.get(1))) < 0.0005;
        if (isEqual)
            function.cmpres = 1;
        else
            function.cmpres = 0;
    }
}
