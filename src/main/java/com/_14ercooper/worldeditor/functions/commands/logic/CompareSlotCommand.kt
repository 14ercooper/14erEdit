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