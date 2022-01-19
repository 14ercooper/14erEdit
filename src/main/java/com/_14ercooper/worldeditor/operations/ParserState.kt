// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations

import org.bukkit.command.CommandSender

class ParserState(val currentPlayer: CommandSender, var parts: List<String>) {
    var index = -1
    var inSetNode = false

    var testDummyState : OperatorState? = null
}