package com.fourteener.worldeditor.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;
import java.util.Set;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.worldeditor.scripts.Craftscript;

public class ScriptBiome extends Craftscript {

	@Override
	public Set<BlockState> perform(LinkedList<String> args, Player player, String label) {
		String radius = args.get(1);
		String biome = args.get(0);
		player.performCommand("fx br s 0 0.5 $ biome{" + radius + ";" + biome + "}");
		return null;
	}

}
