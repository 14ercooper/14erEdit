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

package com._14ercooper.worldeditor.player

import com._14ercooper.worldeditor.brush.Brush
import com._14ercooper.worldeditor.main.Main
import com._14ercooper.worldeditor.selection.SelectionWand
import org.bukkit.Bukkit
import org.bukkit.Material

class PlayerWrapper(val playerName: String) {
    var brushMask = mutableSetOf<Material>()

    init {
        brushMask.add(Material.AIR)
        brushMask.add(Material.CAVE_AIR)
        brushMask.add(Material.VOID_AIR)
    }

    var isDebug = false

    var minEditX: Long = Long.MIN_VALUE
    var minEditY: Long = -1
    var minEditZ: Long = Long.MIN_VALUE
    var maxEditX: Long = Long.MAX_VALUE
    var maxEditY: Long = 256
    var maxEditZ: Long = Long.MAX_VALUE

    init {
        if (Main.majorVer >= 17) {
            minEditY = Bukkit.getServer().worlds[0].minHeight.toLong() - 1
            maxEditY = Bukkit.getServer().worlds[0].maxHeight.toLong() + 1
        }
    }

    fun outsideEditRegion(x: Long, y: Long, z: Long): Boolean {
        return (x <= minEditX || y <= minEditY || z <= minEditZ
                || x >= maxEditX || y >= maxEditY || z >= maxEditZ)
    }

    var brushes = mutableListOf<Brush>()

    var selectionWand = SelectionWand()

    init {
        selectionWand.owner = playerName
    }

    var globalMask = mutableListOf<Material>()

    fun shouldEdit(material: Material): Boolean {
        return globalMask.size == 0 || globalMask.contains(material)
    }

    fun resetGlobalMask(): Unit {
        globalMask = mutableListOf()
    }
}