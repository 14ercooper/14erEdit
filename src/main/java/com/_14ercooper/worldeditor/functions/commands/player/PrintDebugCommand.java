package com._14ercooper.worldeditor.functions.commands.player;

import java.util.List;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;
import com._14ercooper.worldeditor.main.Main;

public class PrintDebugCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	String message = String.join(" ", args);
	Main.logDebug(message);
    }
}
