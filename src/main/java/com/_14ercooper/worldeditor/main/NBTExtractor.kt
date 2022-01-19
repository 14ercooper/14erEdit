// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


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