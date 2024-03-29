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

package com._14ercooper.worldeditor.functions.commands.world

import com._14ercooper.worldeditor.functions.Function
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand
import com._14ercooper.worldeditor.main.Main.Companion.logError
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player

class BlockCommand : InterpreterCommand() {
    override fun run(args: List<String>, function: Function) {
        val blocks = args[0].split(";").toTypedArray()
        val x = function.parseVariable(args[1]).toInt()
        val y = function.parseVariable(args[2]).toInt()
        val z = function.parseVariable(args[3]).toInt()
        val blk: Block =
            (if (function.player is Player) function.player.world else Bukkit.getServer().worlds[0]).getBlockAt(x, y, z)
        var returnVal = false
        for (s in blocks) {
            val m = Material.matchMaterial(s)
            if (!s.contains("#")) {
                if (m == null) {
                    logError("Material $s not found.", function.player, null)
                } else {
                    returnVal = returnVal || m == blk.type
                }
            } else {
                returnVal = returnVal || blk.type.toString().toLowerCase().contains(s.substring(1).toLowerCase())
            }
        }
        if (returnVal) function.cmpres = 1.0 else function.cmpres = 0.0
    }
}