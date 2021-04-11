package com._14ercooper.worldeditor.functions.commands.player

import com._14ercooper.worldeditor.functions.Function
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand
import com._14ercooper.worldeditor.main.Main.Companion.logError

class PrintErrorCommand : InterpreterCommand() {
    override fun run(args: List<String>, function: Function) {
        val message = java.lang.String.join(" ", args)
        logError(message, function.player, null)
    }
}