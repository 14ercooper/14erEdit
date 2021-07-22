package com._14ercooper.worldeditor.macros

import com._14ercooper.worldeditor.async.AsyncManager
import com._14ercooper.worldeditor.macros.macros.Macro
import com._14ercooper.worldeditor.main.Main.Companion.logDebug
import com._14ercooper.worldeditor.main.Main.Companion.logError
import com._14ercooper.worldeditor.operations.OperatorState
import com._14ercooper.worldeditor.undo.UndoElement
import org.bukkit.Location
import java.lang.NullPointerException

object MacroLauncher {
    @JvmField
    var undoElement: UndoElement? = null

    val macros: MutableMap<String, Macro> = HashMap()

    // This allows for macros to be launched and executed
    fun launchMacro(macro: String, location: Location?, undo: UndoElement?, state: OperatorState): Boolean {
        logDebug("Launching macro $macro") // ----
        undoElement = undo
        // First off, parse the macro for the macro name and arguments
        val split1 = macro.split("\\{".toRegex()).toTypedArray()
        val macroName = split1[0]
        val temp1: String
        temp1 = try {
            split1[1].replace("}", "")
        } catch (e: IndexOutOfBoundsException) {
            logError("Could not parse the macro. Did you provide arguments in {}?", state.currentPlayer, e)
            return false
        }
        val macroArgs = temp1.split(";".toRegex()).toTypedArray()
        AsyncManager.countEdits = true
        return try {
            val returnVal = macros[macroName]!!.performMacro(macroArgs, location, state)
            AsyncManager.countEdits = false
            returnVal
        } catch (e : NullPointerException) {
            // Do nothing, this is a test
            false
        }
    }

    fun addMacro(name: String, macro: Macro) {
        if (macros.containsKey(name)) {
            return
        }
        macros[name] = macro
    }
}