package com.fourteener.worldeditor.macros.macros.technical;

import org.bukkit.Location;

import com.fourteener.worldeditor.macros.macros.Macro;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.selection.SchematicHandler;
public class SchematicMacro extends Macro {
	
	String path = "";
	int xPos = 0, yPos = 0, zPos = 0;
	boolean setAir = false;
	String mirrorOpts = "";
	
	// Create a new macro
	private void SetupMacro(String[] args, Location loc) {
		// Parse the file path
		path = args[0];
		// Parse the offset
		int xOff = Integer.parseInt(args[1]);
		int yOff = Integer.parseInt(args[2]);
		int zOff = Integer.parseInt(args[3]);
		xPos = loc.getBlockX() + xOff;
		yPos = loc.getBlockY() + yOff;
		zPos = loc.getBlockZ() + zOff;
		// Parse setting air
		setAir = Boolean.parseBoolean(args[4]);
		// Parse the mirrorString
		if (args.length > 5)
			mirrorOpts = args[5];
	}
	
	// Run this macro
	public boolean performMacro (String[] args, Location loc) {
		SetupMacro(args, loc);
		int[] origin = {xPos,  yPos,  zPos};
		return SchematicHandler.loadSchematic(path, origin, mirrorOpts, setAir, Operator.currentPlayer);
	}
}
