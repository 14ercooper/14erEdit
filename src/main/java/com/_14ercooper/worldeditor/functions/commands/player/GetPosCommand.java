package com._14ercooper.worldeditor.functions.commands.player;

import java.util.List;

import org.bukkit.Location;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class GetPosCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        int dim = (int) function.parseVariable(args.get(1));
        Location loc = function.isOperator ? Operator.currentBlock.getLocation() : function.player.getLocation();
        if (dim == 0) {
            function.setVariable(args.get(0), loc.getX());
        } else if (dim == 1) {
            function.setVariable(args.get(0), loc.getY());
        } else if (dim == 2) {
            function.setVariable(args.get(0), loc.getZ());
        } else {
            Main.logError("Invalid dimension provided: " + dim, function.player, null);
        }
    }
}
