package com._14ercooper.worldeditor.player

import com._14ercooper.worldeditor.main.Main
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
            minEditY = Bukkit.getServer().worlds[0].minHeight.toLong()
            maxEditY = Bukkit.getServer().worlds[0].maxHeight.toLong()
        }
    }

    fun inEditRegion(x: Long, y: Long, z: Long): Boolean {
        return (x <= minEditX || y <= minEditY || z <= minEditZ
                || x >= maxEditX || y >= maxEditY || z >= maxEditZ)
    }
}