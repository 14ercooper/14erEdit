package com._14ercooper.worldeditor.functions.commands.player;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

public class SwapCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	int slot1 = (int) function.parseVariable(args.get(0));
	int slot2 = (int) function.parseVariable(args.get(1));
	ItemStack first = function.player.getInventory().getItem(slot1);
	ItemStack second = function.player.getInventory().getItem(slot2);
	function.player.getInventory().setItem(slot2, first);
	function.player.getInventory().setItem(slot1, second);
    }
}
