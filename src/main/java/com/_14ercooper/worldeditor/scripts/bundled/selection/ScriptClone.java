package com._14ercooper.worldeditor.scripts.bundled.selection;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class ScriptClone extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, Player player, String label) {
        try {
            int xMin = Integer.parseInt(args.get(0));
            int xMax = Integer.parseInt(args.get(1));
            int yMin = Integer.parseInt(args.get(2));
            int yMax = Integer.parseInt(args.get(3));
            int zMin = Integer.parseInt(args.get(4));
            int zMax = Integer.parseInt(args.get(5));
            int xOff = Integer.parseInt(args.get(6));
            int yOff = Integer.parseInt(args.get(7));
            int zOff = Integer.parseInt(args.get(8));
	    Bukkit.getServer().broadcastMessage("Starting clone...");
	    for (int x = xMin; x <= xMax; x++) {
		for (int y = yMin; y <= yMax; y++) {
		    for (int z = zMin; z <= zMax; z++) {
			Material m = Operator.currentPlayer.getWorld().getBlockAt(x, y, z).getType();
			Block b = Operator.currentPlayer.getWorld().getBlockAt(x + xOff, y + yOff, z + zOff);
			b.setType(m);
		    }
		}
	    }
	    Bukkit.getServer().broadcastMessage("Clone finished...");
        }
	catch (Exception e) {
	    Main.logError("Error performing clone. Did you pass in the correct parameters?", Operator.currentPlayer, e);
    }
    }

}
