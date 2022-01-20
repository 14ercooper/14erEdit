/**
 * This file is part of 14erEdit.
 *
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.worldeditor.macros

import com._14ercooper.worldeditor.async.AsyncManager
import com._14ercooper.worldeditor.macros.macros.Macro
import com._14ercooper.worldeditor.main.Main.Companion.logDebug
import com._14ercooper.worldeditor.main.Main.Companion.logError
import com._14ercooper.worldeditor.operations.OperatorState
import org.bukkit.Location
import java.lang.NullPointerException

object MacroLauncher {
    val macros: MutableMap<String, Macro> = HashMap()

    // This allows for macros to be launched and executed
    fun launchMacro(macro: String, location: Location?, state: OperatorState): Boolean {
        logDebug("Launching macro $macro") // ----
        // First off, parse the macro for the macro name and arguments
        val split1 = macro.split("\\{".toRegex()).toTypedArray()
        val macroName = split1[0]
        val temp1: String = try {
            split1[1].replace("}", "")
        } catch (e: IndexOutOfBoundsException) {
            logError("Could not parse the macro. Did you provide arguments in {}?", state.currentPlayer, e)
            return false
        }
        val macroArgs = temp1.split(";".toRegex()).toTypedArray()
        AsyncManager.countEdits = true
        return try {
            val returnVal = macros[macroName]!!.performMacro(macroArgs, location, state)
            returnVal
        } catch (e : NullPointerException) {
            // Do nothing, this is a test
            false
        }
        finally {
            AsyncManager.countEdits = false
        }
    }

    fun addMacro(name: String, macro: Macro) {
        if (macros.containsKey(name)) {
            return
        }
        macros[name] = macro
    }
}