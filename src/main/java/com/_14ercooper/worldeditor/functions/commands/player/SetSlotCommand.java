package com._14ercooper.worldeditor.functions.commands.player;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SetSlotCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	int slot = 0;
	if (args.get(0).equalsIgnoreCase("hand")) {
	    slot = function.player.getInventory().getHeldItemSlot();
	}
	else {
	    slot = (int) function.parseVariable(args.get(0));
	}

	Material toSet = Material.matchMaterial(args.get(1));
	if (toSet == null) {
	    Main.logError("Item ID not known: " + args.get(1), function.player, null);
	}
	else {
	    function.player.getInventory().setItem(slot, new ItemStack(toSet));
    }
    }
}
