package com._14ercooper.worldeditor.operations

import com._14ercooper.worldeditor.blockiterator.BlockWrapper
import com._14ercooper.worldeditor.main.Main
import com._14ercooper.worldeditor.undo.UndoElement
import com._14ercooper.worldeditor.undo.UserUndo
import org.bukkit.Location
import org.bukkit.command.CommandSender

class DummyState(var currentPlayerThis: CommandSender) : OperatorState(
    BlockWrapper(null, 14, 14, 14),
    currentPlayerThis,
    Main.plugin!!.server.worlds[0],
    UndoElement("temp", UserUndo("dummy")),
    Location(Main.plugin!!.server.worlds[0], 14.0, 14.0, 14.0)
)