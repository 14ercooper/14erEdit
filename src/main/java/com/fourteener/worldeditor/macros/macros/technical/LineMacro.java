package com.fourteener.worldeditor.macros.macros.technical;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.fourteener.worldeditor.macros.macros.Macro;
import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.SetBlock;

public class LineMacro extends Macro {

	@Override
	public boolean performMacro(String[] args, Location loc) {
		int x1, x2, y1, y2, z1, z2;
		double dx, dy, dz;
		Material mat;
		x1 = Integer.parseInt(args[0]);
		y1 = Integer.parseInt(args[1]);
		z1 = Integer.parseInt(args[2]);
		x2 = Integer.parseInt(args[3]);
		y2 = Integer.parseInt(args[4]);
		z2 = Integer.parseInt(args[5]);
		mat = Material.matchMaterial(args[6]);
		dx = (x1 - x2) / 1000f;
		dy = (y1 - y2) / 1000f;
		dz = (z1 - z2) / 1000f;
		double x = x1, y = y1, z = z1;
		for (int i = 0; i < 1000; i++) {
			Block b = GlobalVars.world.getBlockAt((int) x, (int) y, (int) z);
			SetBlock.setMaterial(b, mat);
			x += dx;
			y += dy;
			z += dz;
		}
		return true;
	}

}
