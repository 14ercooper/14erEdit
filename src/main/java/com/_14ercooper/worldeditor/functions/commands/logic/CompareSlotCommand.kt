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

package com._14ercooper.worldeditor.functions.commands.logic

import com._14ercooper.worldeditor.functions.Function
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand
import org.bukkit.Material
import org.bukkit.entity.Player

class CompareSlotCommand : InterpreterCommand() {
    override fun run(args: List<String>, function: Function) {
        if (function.player is Player) {
            val slot: Int = if (args[0].equals("hand", ignoreCase = true)) {
                -1
            } else {
                args[0].toInt()
            }
            val handItem: Material? =
                if (slot >= 0) function.player.inventory.getItem(slot)
                    ?.type else function.player.inventory
                    .itemInMainHand.type
            val elements = args[1].split(";").toTypedArray()
            var foundMatch = false
            for (s in elements) {
                foundMatch = if (s.contains("#")) {
                    foundMatch || handItem.toString().toLowerCase().contains(s.substring(1).toLowerCase())
                } else {
                    foundMatch || handItem == Material.matchMaterial(s)
                }
            }
            if (foundMatch) function.cmpres = 1.0 else function.cmpres = 0.0
        }
    }
}