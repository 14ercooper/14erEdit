package com._14ercooper.worldeditor.functions.commands.player

import com._14ercooper.worldeditor.functions.Function
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand
import com._14ercooper.worldeditor.main.Main.Companion.logError
import com._14ercooper.worldeditor.operations.Operator
import org.bukkit.entity.Player

class GetPosCommand : InterpreterCommand() {
    override fun run(args: List<String>, function: Function) {
        if (function.player is Player) {
            val dim = function.parseVariable(args[1]).toInt()
            val loc = if (function.isOperator) Operator.currentBlock!!.location else function.player.location
            when (dim) {
                0 -> {
                    function.setVariable(args[0], loc.x)
                }
                1 -> {
                    function.setVariable(args[0], loc.y)
                }
                2 -> {
                    function.setVariable(args[0], loc.z)
                }
                else -> {
                    logError("Invalid dimension provided: $dim", function.player, null)
                }
            }
        }
    }
}