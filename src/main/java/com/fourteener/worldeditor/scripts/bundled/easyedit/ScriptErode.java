package com.fourteener.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;

import org.bukkit.entity.Player;

import com.fourteener.worldeditor.scripts.Craftscript;

public class ScriptErode extends Craftscript {

	@Override
	public boolean perform(LinkedList<String> args, Player player, String label) {
		String radius = args.get(0);
		String modeArg = args.get(1);
		String mode = "";
		if (modeArg.equalsIgnoreCase("cut") || modeArg.equalsIgnoreCase("raise") || modeArg.equalsIgnoreCase("smooth") || modeArg.equalsIgnoreCase("lift") || modeArg.equalsIgnoreCase("carve")) {
			mode = "melt";
		}
		return player.performCommand("fx br s 0 0.5 $ erode{" + radius + ";" + mode + ";" + modeArg + "}");
	}

}
