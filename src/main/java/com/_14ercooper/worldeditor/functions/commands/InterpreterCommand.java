package com._14ercooper.worldeditor.functions.commands;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;

public abstract class InterpreterCommand {

    // This gets called when a function is run
    public abstract void run(List<String> args, Function function);
}
