// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.functions.commands.player

import com._14ercooper.worldeditor.functions.Function
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand
import com._14ercooper.worldeditor.main.Main.Companion.logDebug

class PrintDebugCommand : InterpreterCommand() {
    override fun run(args: List<String>, function: Function) {
        val message = java.lang.String.join(" ", args)
        logDebug(message)
    }
}