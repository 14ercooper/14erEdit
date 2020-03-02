package fourteener.worldeditor.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import fourteener.worldeditor.worldeditor.scripts.Craftscript;

public class ScriptVines extends Craftscript {

	@Override
	public List<BlockState> perform(LinkedList<String> args, Player player, String label) {
		String radius = args.get(0);
		String length = args.get(1);
		String variance = args.get(2);
		String density;
		if (args.size() > 3) {
			density = args.get(3);
		}
		else {
			density = "0.2";
		}
		player.performCommand("fx br s 0 0.5 $ vines{" + radius + ";" + length + ";" + variance + ";" + density + "}");
		return null;
	}

}
