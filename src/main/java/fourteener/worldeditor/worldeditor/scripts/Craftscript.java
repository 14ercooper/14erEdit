package fourteener.worldeditor.worldeditor.scripts;

import java.util.LinkedList;
import java.util.Set;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public abstract class Craftscript {
	public abstract Set<BlockState> perform (LinkedList<String> args, Player player, String label);
}
