// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.functions.commands.player

import com._14ercooper.worldeditor.functions.Function
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand
import com._14ercooper.worldeditor.main.Main.Companion.logError
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SetSlotCommand : InterpreterCommand() {
    override fun run(args: List<String>, function: Function) {
        if (function.player is Player) {
            val slot: Int = if (args[0].equals("hand", ignoreCase = true)) {
                function.player.inventory.heldItemSlot
            } else {
                function.parseVariable(args[0]).toInt()
            }
            val toSet = Material.matchMaterial(args[1])
            if (toSet == null) {
                logError("Item ID not known: " + args[1], function.player, null)
            } else {
                function.player.inventory.setItem(slot, ItemStack(toSet))
            }
        }
    }
}