package fourteener.worldeditor.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.worldeditor.scripts.Craftscript;

public class ScriptOverlay extends Craftscript {

	// Args block depth air
	@Override
	public List<BlockState> perform(LinkedList<String> args, Player player, String label) {
		String radius = args.get(0);
		String block = args.get(1);
		int depth = Integer.parseInt(args.get(2));
		int air;
		if (args.size() > 3) {
			air = Integer.parseInt(args.get(3));
		}
		else {
			air = 3;
		}
		
		String command = "fx br s " + radius + " 0.5 ? ~ air ";
		for (int i = 0; i < depth; i++) {
			command += "| ? ^ - " + (i+1) + " " + (air+i-1) + " air > " + block + " false ";
		}
		command += "false false";
		Main.logDebug("Overlay command: " + command);
		
		player.performCommand(command);
		return null;
	}
}
