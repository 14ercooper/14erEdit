package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class ScriptOverlay extends Craftscript {

    // Args block depth air
    @Override
    public void perform(LinkedList<String> args, Player player, String label) {
        try {
            String radius = args.get(0);
            String block = args.get(1);
            int depth = Integer.parseInt(args.get(2));
            int air;
            if (args.size() > 3) {
                air = Integer.parseInt(args.get(3));
            } else {
                air = 3;
            }

            StringBuilder command = new StringBuilder("fx br s " + radius + " 0.5 ? ~ air ");
            for (int i = 0; i < depth; i++) {
                command.append("| ? ^ - ").append(i + 1).append(" ").append(air + i - 1).append(" air > ").append(block).append(" : false ");
            }
            command.append(": false : false");
            Main.logDebug("Overlay command: " + command);

            player.performCommand(command.toString());
        } catch (Exception e) {
            Main.logError("Could not parse overlay macro. Did you provide the correct arguments?",
                    Operator.currentPlayer, e);
        }
    }
}
