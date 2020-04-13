package com.fourteener.worldeditor.macros.macros.technical;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.fourteener.worldeditor.macros.macros.Macro;
import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.main.SetBlock;

public class CatenaryMacro extends Macro {

	@Override
	public boolean performMacro(String[] args, Location loc) {
		
		double x0 = Double.parseDouble(args[0]), y0 = Double.parseDouble(args[1]),
				z0 = Double.parseDouble(args[2]), dx = Double.parseDouble(args[3]),
				dy = Double.parseDouble(args[4]), dy2 = Double.parseDouble(args[5]),
				dz = Double.parseDouble(args[6]), step = Double.parseDouble(args[7]);
		String block = args[8];
		
		Main.logDebug("Performing catenary with parameters: " + x0 + "," + x0 + "," + y0 + "," + z0 + "," + dx + "," + dy + "," + dy2 + "," + dz + "," + step + "," + block);
		
		double t = 0f;
		Main.logDebug("" + (1f + (step / 2f)));
		for (; t < 1f + (step / 2f); t += step) {
			int x = (int) (x0 + (t * dx));
			int y = (int) (y0 + (t * dy) + (t * t * dy2));
			int z = (int) (z0 + (t * dz));
			Main.logDebug(x + "," + y + "," + z);
			Block b = GlobalVars.world.getBlockAt(x, y, z);
			SetBlock.setMaterial(b, Material.matchMaterial(block));
		}
		
		return true;
	}

}
