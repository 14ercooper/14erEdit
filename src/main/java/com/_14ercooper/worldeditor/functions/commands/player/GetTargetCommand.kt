package com._14ercooper.worldeditor.functions.commands.player

import com._14ercooper.worldeditor.functions.Function
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand
import com._14ercooper.worldeditor.main.Main.Companion.logError
import org.bukkit.entity.Player

class GetTargetCommand : InterpreterCommand() {
    override fun run(args: List<String>, function: Function) {
        if (function.player is Player) {
            when (val dim = function.parseVariable(args[1]).toInt()) {
                0 -> {
                    function.setVariable(args[0], function.target.location.x)
                }
                1 -> {
                    function.setVariable(args[0], function.target.location.y)
                }
                2 -> {
                    function.setVariable(args[0], function.target.location.z)
                }
                else -> {
                    logError("Invalid dimension provided: $dim", function.player, null)
                }
            }
        }
    }
}