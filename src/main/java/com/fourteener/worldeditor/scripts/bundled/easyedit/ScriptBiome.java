package com.fourteener.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;

import org.bukkit.entity.Player;

import com.fourteener.worldeditor.scripts.Craftscript;

public class ScriptBiome extends Craftscript {

	@Override
	public boolean perform(LinkedList<String> args, Player player, String label) {
		String radius = args.get(1);
		String biome = args.get(0);
		return player.performCommand("fx br s 0 0.5 $ biome{" + radius + ";" + biome + "}");
	}

}
