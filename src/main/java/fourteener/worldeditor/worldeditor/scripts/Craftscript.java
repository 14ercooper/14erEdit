package fourteener.worldeditor.worldeditor.scripts;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public abstract class Craftscript {
	public abstract List<BlockState> perform (LinkedList<String> args, Player player);
}
