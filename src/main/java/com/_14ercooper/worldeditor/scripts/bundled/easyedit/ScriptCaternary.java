package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;

import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;
import com._14ercooper.worldeditor.selection.SelectionManager;

public class ScriptCaternary extends Craftscript {

	@Override
	public boolean perform(LinkedList<String> args, Player player, String label) {
		try {
			String block = args.get(0);
			double step = 0.01;
			double droop = Double.parseDouble(args.get(1));
			if (args.size() > 2) {
				step = Double.parseDouble(args.get(2));
			}

			double[] negativeCorner = SelectionManager.getSelectionManager(player).getPositionOne();
			double[] positiveCorner = SelectionManager.getSelectionManager(player).getPositionTwo();
			double midpointY = ((positiveCorner[1] + negativeCorner[1]) * 0.5) - droop;

			double x0 = negativeCorner[0];
			double dx = positiveCorner[0] - negativeCorner[0];
			double z0 = negativeCorner[2];
			double dz = positiveCorner[2] - negativeCorner[2];
			double y0 = negativeCorner[1];
			double dy2 = -4 * (midpointY - (0.5 * negativeCorner[1]) - positiveCorner[1]);
			double dy = positiveCorner[1] - negativeCorner[1] - dy2;

			Main.logDebug("X: " + x0 + " " + dx);
			Main.logDebug("Y: " + y0 + " " + dy + " " + dy2);
			Main.logDebug("Z: " + z0 + " " + dz);

			player.performCommand("run $ catenary{" + x0 + ";" + y0 + ";" + z0 + ";" + dx + ";" + dy + ";" + dy2 + ";" + dz + ";" + step + ";" + block + "}");
		} catch (Exception e) {
			Main.logError("Error running Catenary script. Did you pass the correct arguments?", Operator.currentPlayer);
			return false;
		}

		return true;
	}

}
