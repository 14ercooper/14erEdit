package fourteener.worldeditor.worldeditor.scripts.bundled;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import fourteener.worldeditor.worldeditor.scripts.Craftscript;

public class ScriptBiome extends Craftscript {

	@Override
	public List<BlockState> perform(LinkedList<String> args, Player player) {
		String radius = args.get(1);
		String biome = args.get(0);
		player.performCommand("fx br s 0 0.5 $ biome{" + radius + ";" + biome + "}");
		return null;
	}

}
