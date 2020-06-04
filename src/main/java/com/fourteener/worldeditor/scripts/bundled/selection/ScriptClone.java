package com.fourteener.worldeditor.scripts.bundled.selection;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.scripts.Craftscript;

public class ScriptClone extends Craftscript {

	@Override
	public boolean perform(LinkedList<String> args, Player player, String label) {
		try {
			int xMin  = Integer.parseInt(args.get(0));
			int xMax  = Integer.parseInt(args.get(1));
			int yMin  = Integer.parseInt(args.get(2));
			int yMax  = Integer.parseInt(args.get(3));
			int zMin  = Integer.parseInt(args.get(4));
			int zMax  = Integer.parseInt(args.get(5));
			int xOff  = Integer.parseInt(args.get(6));
			int yOff  = Integer.parseInt(args.get(7));
			int zOff  = Integer.parseInt(args.get(8));
			Bukkit.getServer().broadcastMessage("Starting clone...");
			for (int x = xMin; x <= xMax; x++) {
				for (int y = yMin; y <= yMax; y++) {
					for (int z = zMin; z <= zMax; z++) {
						Material m = GlobalVars.world.getBlockAt(x, y, z).getType();
						Block b = GlobalVars.world.getBlockAt(x + xOff, y + yOff, z + zOff);
						b.setType(m);
					}
				}
			}
			Bukkit.getServer().broadcastMessage("Clone finished...");
			return true;
		} catch (Exception e) {
			Main.logError("Error performing clone. Did you pass in the correct parameters?", Operator.currentPlayer);
			return false;
		}
	}

}
