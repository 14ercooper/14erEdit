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