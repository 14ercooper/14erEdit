package com.fourteener.worldeditor.scripts.bundled.easyedit;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.scripts.Craftscript;
import com.fourteener.worldeditor.selection.SelectionManager;

public class ScriptCaternary extends Craftscript {

	@Override
	public Set<BlockState> perform(LinkedList<String> args, Player player, String label) {
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
		
		Set<BlockState> snapshotArray = new HashSet<BlockState>();
		for (double t = 0; t < 1 + (step / 2); t += step) {
			int x = (int) (x0 + (t * dx));
			int y = (int) (y0 + (t * dy) + (t * t * dy2));
			int z = (int) (z0 + (t * dz));
			Main.logDebug("Pos: " + x + " " + y + " " + z);
			Block b = GlobalVars.world.getBlockAt(x, y, z);
			snapshotArray.add(b.getState());
			b.setType(Material.matchMaterial(block));
		}
		
		return snapshotArray;
	}

}
