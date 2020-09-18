package com._14ercooper.worldeditor.functions.commands.world;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;
import com._14ercooper.worldeditor.main.Main;

public class BlockCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
	String[] blocks = args.get(0).split(";");
	int x = (int) function.parseVariable(args.get(1));
	int y = (int) function.parseVariable(args.get(2));
	int z = (int) function.parseVariable(args.get(3));
	Block blk = function.player.getWorld().getBlockAt(x, y, z);
	boolean returnVal = false;
	for (String s : blocks) {
	    Material m = Material.matchMaterial(s);
	    if (!s.contains("#")) {
		if (m == null) {
		    Main.logError("Material " + s + " not found.", function.player);
		}
		else {
		    returnVal = returnVal || m == blk.getType();
		}
	    }
	    else {
		returnVal = returnVal || blk.getType().toString().toLowerCase().contains(s.substring(1).toLowerCase());
	    }
	}
	if (returnVal) function.cmpres = 0;
	else function.cmpres = 1;
    }
}
