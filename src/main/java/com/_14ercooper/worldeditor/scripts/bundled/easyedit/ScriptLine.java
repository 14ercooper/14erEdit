package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class ScriptLine extends Craftscript {

    // Args block depth air
    @Override
    public void perform(LinkedList<String> args, Player player, String label) {
        try {
            String block = args.get(0);
            int length;
            if (args.size() > 1) {
                length = Integer.parseInt(args.get(1));
            } else {
                length = 10;
            }

            Vector blockPos = player.getLocation().getDirection();
            Vector playerPos = player.getLocation().toVector();

            int x1 = (int) playerPos.getX();
            int y1 = (int) playerPos.getY() + 1;
            int z1 = (int) playerPos.getZ();
            int x2 = (int) (blockPos.getX() * -1 * length + playerPos.getX());
            int y2 = (int) (blockPos.getY() * -1 * length + playerPos.getY());
            int z2 = (int) (blockPos.getZ() * -1 * length + playerPos.getZ());

            player.performCommand(
                    "run $ line_old{" + x1 + ";" + y1 + ";" + z1 + ";" + x2 + ";" + y2 + ";" + z2 + ";" + block + "}");

        } catch (Exception e) {
            Main.logError("Could not parse line macro. Did you provide a block material and optionally length?",
                    Operator.currentPlayer, e);
        }
    }
}
