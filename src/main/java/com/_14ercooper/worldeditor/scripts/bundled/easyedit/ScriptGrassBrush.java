package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;

import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;

public class ScriptGrassBrush extends Craftscript {

    @Override
    public boolean perform(LinkedList<String> args, Player player, String label) {
	try {
	    String radius = args.get(0);
	    String mixture, airSpaces, density;
	    if (args.size() >= 2) {
		mixture = args.get(1);
		if (args.size() >= 3) {
		    airSpaces = args.get(2);
		    if (args.size() >= 4) {
			density = args.get(3);
		    }
		    else {
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

	    return player.performCommand(
		    "fx br s 0 0.5 $ grass{" + radius + ";" + mixture + ";" + airSpaces + ";" + density + "}");
	}
	catch (Exception e) {
	    Main.logError("Could not parse grass brush macro. Did you provide the correct arguments?",
		    Operator.currentPlayer);
	    return false;
	}
    }
}
