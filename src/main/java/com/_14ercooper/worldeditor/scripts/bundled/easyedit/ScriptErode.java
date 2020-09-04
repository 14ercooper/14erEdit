package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;

import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;

public class ScriptErode extends Craftscript {

    @Override
    public boolean perform(LinkedList<String> args, Player player, String label) {
	try {
	    String radius = args.get(0);
	    String modeArg = args.get(1);
	    String mode = "";
	    if (modeArg.equalsIgnoreCase("cut") || modeArg.equalsIgnoreCase("raise")
		    || modeArg.equalsIgnoreCase("smooth") || modeArg.equalsIgnoreCase("lift")
		    || modeArg.equalsIgnoreCase("carve")) {
		mode = "melt";
	    }
	    return player.performCommand("fx br s 0 0.5 $ erode{" + radius + ";" + mode + ";" + modeArg + "}");
	}
	catch (Exception e) {
	    Main.logError("Error parsing erode macro. Did you pass in the correct arguments?", Operator.currentPlayer);
	    return false;
	}
    }

}
