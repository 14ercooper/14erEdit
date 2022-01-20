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
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SwapCommand : InterpreterCommand() {
    override fun run(args: List<String>, function: Function) {
        if (function.player is Player) {
            val slot1 = function.parseVariable(args[0]).toInt()
            val slot2 = function.parseVariable(args[1]).toInt()
            val first: ItemStack? = function.player.inventory.getItem(slot1)
            val second: ItemStack? = function.player.inventory.getItem(slot2)
            function.player.inventory.setItem(slot2, first)
            function.player.inventory.setItem(slot1, second)
        }
    }
}