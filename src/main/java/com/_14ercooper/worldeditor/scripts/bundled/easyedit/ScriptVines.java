package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;

import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;

public class ScriptVines extends Craftscript {

	@Override
	public boolean perform(LinkedList<String> args, Player player, String label) {
		try {
			String radius = args.get(0);
			String length = args.get(1);
			String variance = args.get(2);
			String density, block = "";
			if (args.size() > 3) {
				density = args.get(3);
				if (args.size() > 4) {
					block = args.get(4);
				}
				else {
					block = "vine";
				}
			}
			else {
				density = "0.2";
			}
			return player.performCommand("fx br s 0 0.5 $ vines{" + radius + ";" + length + ";" + variance + ";" + density + ";" + block + "}");
		} catch (Exception e) {
			Main.logError("Could not parse vine script. Did you pass the correct arguments?", Operator.currentPlayer);
			return false;
		}
	}

}
