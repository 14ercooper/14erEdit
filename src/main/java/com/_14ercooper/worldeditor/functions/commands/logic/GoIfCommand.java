package com._14ercooper.worldeditor.functions.commands.logic;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class GoIfCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        if (Math.abs(function.cmpres) > 0.01) {
            if (args.size() > 1 && Boolean.parseBoolean(args.get(1)))
                function.ra = function.currentLine;
            function.currentLine = function.labelsMap.get(args.get(0));
        }
    }
}
