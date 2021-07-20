package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.scripts.Craftscript;
import com._14ercooper.worldeditor.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class ScriptCaternary extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, CommandSender player, String label) {
        try {
            String block = args.get(0);
            double step = 0.01;
            double droop = Double.parseDouble(args.get(1));
            if (args.size() > 2) {
                step = Double.parseDouble(args.get(2));
            }

            double[] negativeCorner, positiveCorner;
            if (player instanceof Player) {
                negativeCorner = SelectionManager.getSelectionManager(((Player) player).getUniqueId()).getPositionOne();
                positiveCorner = SelectionManager.getSelectionManager(((Player) player).getUniqueId()).getPositionTwo();
            } else {
                positiveCorner = new double[]{0, 0, 0};
                negativeCorner = new double[]{0, 0, 0};
            }
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

            Bukkit.getServer().dispatchCommand(player, "run $ catenary{" + x0 + ";" + y0 + ";" + z0 + ";" + dx + ";" + dy + ";" + dy2 + ";"
                    + dz + ";" + step + ";" + block + "}");
        } catch (Exception e) {
            Main.logError("Error running Catenary script. Did you pass the correct arguments?", player, e);
        }

    }

}
