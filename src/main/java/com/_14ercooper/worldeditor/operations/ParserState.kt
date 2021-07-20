package com._14ercooper.worldeditor.operations

import org.bukkit.command.CommandSender

class ParserState (val currentPlayer : CommandSender, var parts : List<String>) {
    var index = -1
    var inSetNode = false
}