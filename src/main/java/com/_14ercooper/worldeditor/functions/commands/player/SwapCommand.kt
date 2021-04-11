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