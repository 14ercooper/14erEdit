package com.fourteener.worldeditor.scripts.bundled.selection;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.scripts.Craftscript;

public class ScriptMegaoperate extends Craftscript {

	@Override
	public boolean perform(LinkedList<String> args, Player player, String label) {
		int xMin  = Integer.parseInt(args.get(0));
		int xMax  = Integer.parseInt(args.get(1));
		int yMin  = Integer.parseInt(args.get(2));
		int yMax  = Integer.parseInt(args.get(3));
		int zMin  = Integer.parseInt(args.get(4));
		int zMax  = Integer.parseInt(args.get(5));
		String op  = args.get(6);
		Operator operator = new Operator(op);
		Bukkit.getServer().broadcastMessage("Starting operation...");
		for (int x = xMin; x <= xMax; x++) {
			for (int y = yMin; y <= yMax; y++) {
				for (int z = zMin; z <= zMax; z++) {
					operator.operateOnBlock(GlobalVars.world.getBlockAt(x, y, z));
				}
			}
		}
		Bukkit.getServer().broadcastMessage("Operation finished...");
		return true;
	}

}
