// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.selection;

import com._14ercooper.math.Point3;
import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class MultiselectCommandHandler {

    public static boolean handleCommand(SelectionManager manager, String player, String[] args) {
        if (args[1].equalsIgnoreCase("spline") || args[1].equalsIgnoreCase("uniformspline")
                || args[1].equalsIgnoreCase("chordalspline")) {

            Operator op = parseOp(args, 2, player);

            List<String> iteratorArgs = new ArrayList<>();
            if (args[1].equalsIgnoreCase("spline")) {
                iteratorArgs.add("0.5");
            }
            else if (args[1].equalsIgnoreCase("uniformspline")) {
                iteratorArgs.add("0");
            }
            else if (args[1].equalsIgnoreCase("chordalspline")) {
                iteratorArgs.add("1");
            }

            // Convert all points to args
            for (Point3 point : manager.getAllPoints()) {
                iteratorArgs.add(point.toString());
            }

            BlockIterator iter = Objects.requireNonNull(IteratorManager.INSTANCE.getIterator("spline")).newIterator(iteratorArgs,
                    Objects.requireNonNull(Bukkit.getServer().getPlayer(UUID.fromString(player))).getWorld(), Bukkit.getConsoleSender());

            AsyncManager.scheduleEdit(op, Bukkit.getServer().getPlayer(UUID.fromString(player)), iter);
            return true;
        }
        return false;
    }

    public static Operator parseOp(String[] args, int startIndex, String player) {
        Player p = Bukkit.getServer().getPlayer(UUID.fromString(player));

        List<String> opArray = new LinkedList<>(Arrays.asList(args));
        while (startIndex > 0) {
            opArray.remove(0);
            startIndex--;
        }

        StringBuilder opStr = new StringBuilder();
        for (String s : opArray) {
            opStr.append(s);
            opStr.append(" ");
        }

        return new Operator(opStr.toString(), p);
    }
}
