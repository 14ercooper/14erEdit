package com.fourteener.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;
import java.util.Set;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.scripts.Craftscript;

public class ScriptTree extends Craftscript{

	@Override
	public Set<BlockState> perform(LinkedList<String> args, Player player, String label) {
		String treeType = args.get(0);
		String treeSize = args.get(1);
		String treeLeaves;
		String treeWood;
		double treeSizeVariance = Double.parseDouble(treeSize) * 0.25;
		if (args.size() == 2) {
			treeLeaves = "lime_wool";
			treeWood = "brown_wool";
		}
		else {
			treeLeaves = args.get(2);
			treeWood = args.get(3);
		}
		player.performCommand("fx br s 0 0.5 $ tree{" + treeType + ";" + treeLeaves + ";" + treeWood + ";" + treeSize + ";" + treeSizeVariance + "}");
		return null;
	}
}