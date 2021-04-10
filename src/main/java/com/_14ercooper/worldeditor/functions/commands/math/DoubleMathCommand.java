package com._14ercooper.worldeditor.functions.commands.math;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;
import com._14ercooper.worldeditor.main.Main;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class DoubleMathCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        String funct = args.get(0);
        double num1 = function.parseVariable(args.get(1));
        double num2 = function.parseVariable(args.get(2));
        double result = 0;
        try {
            result = (double) Math.class.getMethod(funct, Double.class, Double.class).invoke(num1, num2);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | IllegalAccessException e) {
            Main.logError("Invalid math function: " + funct, function.player, e);
        }
        function.setVariable(args.get(3), result);
    }
}
