package com.fourteener.worldeditor.selection;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.entity.Player;

import com.fourteener.schematics.SchemLite;
import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;

public class SchematicHandler {
	// Save a schematic to disk
	public static boolean saveSchematic (String file, Player p) {
		Main.logDebug("Saving schematic to " + file);
		SelectionManager sm = SelectionManager.getSelectionManager(p);
		double[] rawOrigin = sm.getMostNegativeCorner();
		double[] posCorner = sm.getMostPositiveCorner();
		int[] origin = {(int) rawOrigin[0], (int) rawOrigin[1], (int) rawOrigin[2]};
		int xSize = (int) Math.abs(posCorner[0] - rawOrigin[0] + 1);
		int ySize = (int) Math.abs(posCorner[1] - rawOrigin[1] + 1);
		int zSize = (int) Math.abs(posCorner[2] - rawOrigin[2] + 1);
		String path = file;
		String author = p.getDisplayName();
		String date = (new SimpleDateFormat("yyyy-mm-dd")).format(Calendar.getInstance().getTime());
		SchemLite schem = new SchemLite(xSize, ySize, zSize, path, author, date);
		GlobalVars.asyncManager.scheduleEdit(schem, true, p, origin);
		return true;
	}
	
	// Load a schematic into the world
	public static boolean loadSchematic (String file, Player p, String mirror, boolean setAir) {
		Main.logDebug("Loading schematic from " + file);
		int[] origin = {p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()};
		String path = file;
		SchemLite schem = new SchemLite(path, setAir);
		try {
			schem.openRead();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!mirror.isEmpty()) {
			schem.mirror(mirror.contains("x"), mirror.contains("y"), mirror.contains("z"));
		}
		GlobalVars.asyncManager.scheduleEdit(schem, false, p, origin);
		return true;
	}
	
	// Load a schematic into the world with offset
	public static boolean loadSchematic (String file, Player p, String mirror, boolean setAir, int[] offset) {
		Main.logDebug("Loading schematic from " + file);
		int[] origin = {p.getLocation().getBlockX() + offset[0], p.getLocation().getBlockY() + offset[1], p.getLocation().getBlockZ() + offset[2]};
		String path = file;
		SchemLite schem = new SchemLite(path, setAir);
		try {
			schem.openRead();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!mirror.isEmpty()) {
			schem.mirror(mirror.contains("x"), mirror.contains("y"), mirror.contains("z"));
		}
		GlobalVars.asyncManager.scheduleEdit(schem, false, p, origin);
		return true;
	}
}
