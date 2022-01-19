// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.functions.commands.logic;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class ExitCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        function.exit = true;
        if (args.size() > 0) {
            if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("false")) {
                if (Boolean.parseBoolean(args.get(0)))
                    function.exitVal = 1;
                else function.exitVal = 0;
//		Main.logDebug("Function returning as boolean with value " + args.get(0));
            } else {
                try {
                    function.exitVal = function.parseVariable(args.get(0));
//		    Main.logDebug("Function returning as variable with value " + function.exitVal);
                } catch (Exception e) {
                    function.exitVal = Double.parseDouble(args.get(0));
//		    Main.logDebug("Function returning as double with value " + function.exitVal);
                }
            }
        }
    }
}
