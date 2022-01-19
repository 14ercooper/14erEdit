// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.functions.commands;

import com._14ercooper.worldeditor.functions.Function;

import java.util.List;

public abstract class InterpreterCommand {

    // This gets called when a function is run
    public abstract void run(List<String> args, Function function);
}
