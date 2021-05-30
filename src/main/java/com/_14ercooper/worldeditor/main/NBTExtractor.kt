package com._14ercooper.worldeditor.main

import org.bukkit.block.Block
import org.bukkit.block.BlockState

class NBTExtractor {
    fun getNBT(bs: BlockState): String {
        return if (!bs.javaClass.name.endsWith("CraftBlockState")) {
            val craftbukkitApiVer = if (GlobalVars.majorVer == 13) {
                if (GlobalVars.minorVer == 0) {
                    "1_13_R1"
                }
                else {
                    "1_13_R2"
                }
            }
            else if (GlobalVars.majorVer == 14) {
                "1_14_R1"
            }
            else if (GlobalVars.majorVer == 15) {
                "1_15_R1"
            }
            else if (GlobalVars.majorVer == 16) {
                if (GlobalVars.minorVer == 0 || GlobalVars.minorVer == 1) {
                    "1_16_R1"
                }
                else if (GlobalVars.minorVer == 2 || GlobalVars.minorVer == 3) {
                    "1_16_R2"
                }
                else {
                    "1_16_R3"
                }
            }
            else if (GlobalVars.majorVer == 17) {
                "1_17_R1"
            }
            else {
                "1_16_R3"
            }
            val className = "org.bukkit.craftbukkit.v$craftbukkitApiVer.block.CraftBlockEntityState"
            val unsafeClass : Class<*> = Class.forName(className)
            val cb = unsafeClass.cast(bs)
            val ntc = unsafeClass.getMethod("getSnapshotNBT").invoke(cb)
            ntc.javaClass.getMethod("asString").invoke(ntc) as String
        } else {
            ""
        }
    }

    fun getNBT(b: Block): String {
        return getNBT(b.state)
    }
}