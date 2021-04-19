package com._14ercooper.worldeditor.functions.commands.logic;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class ReturnCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        function.currentLine = (int) function.ra;
    }
}
