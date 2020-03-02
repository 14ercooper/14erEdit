package fourteener.worldeditor.worldeditor.scripts.bundled;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import fourteener.worldeditor.worldeditor.scripts.Craftscript;

public class ScriptGrassBrush extends Craftscript {

	@Override
	public List<BlockState> perform(LinkedList<String> args, Player player, String label) {
		String radius = args.get(0);
		String mixture, airSpaces, density;
		if (args.size() >= 2) {
			mixture = args.get(1);
			if (args.size() >= 3) {
				airSpaces = args.get(2);
				if (args.size() >= 4) {
					density = args.get(3);
				} else {
					density = "0.35";
				}
			}
			else {
				airSpaces = "3";
				density = "0.35";
			}
		}
		else {
			mixture = "60%grass,25%poppy,15%dandelion";
			airSpaces = "3";
			density = "0.35";
		}
		
		player.performCommand("fx br s 0 0.5 $ grass{" + radius + ";" + mixture + ";" + airSpaces + ";" + density + "}");
		return null;
	}
}
