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

package com._14ercooper.worldeditor.main

import org.bukkit.block.Block
import org.bukkit.block.BlockState

class NBTExtractor {
    fun getNBT(bs: BlockState): String {
        return if (!bs.javaClass.name.endsWith("CraftBlockState")) {
            val craftbukkitApiVer = getServerVersionId()
            val className = "org.bukkit.craftbukkit.v$craftbukkitApiVer.block.CraftBlockEntityState"
            val unsafeClass: Class<*> = Class.forName(className)
            val cb = unsafeClass.cast(bs)
            val ntc = unsafeClass.getMethod("getSnapshotNBT").invoke(cb)
            try {
                ntc.javaClass.getMethod("asString").invoke(ntc) as String
            }
            catch (e: Exception) {
                ntc.javaClass.getMethod("toString").invoke(ntc) as String
            }
        } else {
            ""
        }
    }

    var versionId: String? = null
    fun getServerVersionId(): String {
        if (versionId != null) {
            return versionId as String
        }
        for (majorMajor in 1..10) {
            for (major in 0..100) {
                for (minor in 0..25) {
                    var didCrash = false
                    try {
                        versionId = "${majorMajor}_${major}_R${minor}"
                        Class.forName("org.bukkit.craftbukkit.v$versionId.block.CraftBlockEntityState")
                    } catch (e: ClassNotFoundException) {
                        didCrash = true
                    }
                    if (!didCrash) {
                        return versionId as String
                    }
                }
            }
        }
        Main.logError("Could not load NBT class", null, null)
        return ""
    }

    fun getNBT(b: Block): String {
        return getNBT(b.state)
    }
}