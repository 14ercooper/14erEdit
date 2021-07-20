package com._14ercooper.worldeditor.operations

import com._14ercooper.worldeditor.main.Main
import com._14ercooper.worldeditor.undo.UndoElement
import com._14ercooper.worldeditor.undo.UserUndo
import org.bukkit.command.CommandSender

class DummyState (var currentPlayerThis : CommandSender) : OperatorState(Main.plugin.server.worlds[0].getBlockAt(0, 0, 0), currentPlayerThis, Main.plugin.server.worlds[0], UndoElement("temp", UserUndo("dummy"))) {
}