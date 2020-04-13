package com.fourteener.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.fourteener.worldeditor.scripts.Craftscript;

public class ScriptLine extends Craftscript {

	// Args block depth air
	@Override
	public boolean perform(LinkedList<String> args, Player player, String label) {
		String block = args.get(0);
		int length;
		if (args.size() > 1) {
			length = Integer.parseInt(args.get(1));
		}
		else {
			length = Integer.MAX_VALUE;
		}

		Vector blockPos = player.getLocation().getDirection();
		Vector playerPos = player.getLocation().toVector();
		
		int x1 = (int) playerPos.getX();
		int y1 = (int) playerPos.getY() + 1;
		int z1 = (int) playerPos.getZ();
		int x2 = (int)(blockPos.getX() * -1 * length + playerPos.getX());
		int y2 = (int)(blockPos.getY() * -1 * length + playerPos.getY());
		int z2 = (int)(blockPos.getZ() * -1 * length + playerPos.getZ());
		
		player.performCommand("run $ line{" + x1 + ";" + y1 + ";" + z1 + ";" + x2 + ";" + y2 + ";" + z2 + ";" + block + "}");
		
		return true;
	}
}
