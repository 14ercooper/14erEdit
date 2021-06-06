package com._14ercooper.worldeditor.macros.macros.technical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.scripts.CraftscriptManager;
import com._14ercooper.worldeditor.undo.UndoElement;
import com._14ercooper.worldeditor.undo.UndoSystem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.Operator;

public class LineMacro extends Macro {

    @Override
    public boolean performMacro(String[] args, Location loc) {
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0, z1 = 0, z2 = 0;
        double dx, dy, dz;
        Material mat = null;
        try {
            x1 = Integer.parseInt(args[0]);
            y1 = Integer.parseInt(args[1]);
            z1 = Integer.parseInt(args[2]);
            x2 = Integer.parseInt(args[3]);
            y2 = Integer.parseInt(args[4]);
            z2 = Integer.parseInt(args[5]);
            mat = Material.matchMaterial(args[6]);
        } catch (Exception e) {
            Main.logError("Error parsing line macro. Did you provide start and end coordinates and a block material?",
                    Operator.currentPlayer, e);
            return false;
        }
        dx = (x1 - x2) / 1000f;
        dy = (y1 - y2) / 1000f;
        dz = (z1 - z2) / 1000f;

        Main.logDebug("Placing line from " + x1 + "," + y1 + "," + z1 + " in direction " + dx + "," + dy + "," + dz);

        UndoElement undoElement;
        if (GlobalVars.asyncManager.currentAsyncOp == null) {
            undoElement = UndoSystem.findUserUndo(CraftscriptManager.currentPlayer).getNewUndoElement();
        }
        else {
            undoElement = GlobalVars.asyncManager.currentAsyncOp.getUndo();
        }

        double x = x1, y = y1, z = z1;
        for (int i = 0; i < 1000; i++) {
            Block b = Operator.currentWorld.getBlockAt((int) (x + 0.5), (int) (y + 0.5), (int) (z + 0.5));
            SetBlock.setMaterial(b, mat, undoElement);
            x += dx;
            y += dy;
            z += dz;
        }

        undoElement.finalizeUndo();
        return true;
    }

}
