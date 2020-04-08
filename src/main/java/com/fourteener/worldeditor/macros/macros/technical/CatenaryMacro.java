package com.fourteener.worldeditor.macros.macros.technical;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.fourteener.worldeditor.macros.macros.Macro;
import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.SetBlock;

public class CatenaryMacro extends Macro {

	@Override
	public boolean performMacro(String[] args, Location loc) {
		
		double x0 = Integer.parseInt(args[0]), y0 = Integer.parseInt(args[1]),
				z0 = Integer.parseInt(args[2]), dx = Integer.parseInt(args[3]),
				dy = Integer.parseInt(args[4]), dy2 = Integer.parseInt(args[4]),
				dz = Integer.parseInt(args[5]), step = Integer.parseInt(args[6]);
		String block = args[6];
		
		for (double t = 0; t < 1 + (step / 2); t += step) {
			int x = (int) (x0 + (t * dx));
			int y = (int) (y0 + (t * dy) + (t * t * dy2));
			int z = (int) (z0 + (t * dz));
			Block b = GlobalVars.world.getBlockAt(x, y, z);
			SetBlock.setMaterial(b, Material.matchMaterial(block));
		}
		
		return true;
	}

}
