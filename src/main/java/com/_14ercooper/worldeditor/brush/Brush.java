/**
 * This file is part of 14erEdit.
 *
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.worldeditor.brush;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.shapes.Multi;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.player.PlayerManager;
import com._14ercooper.worldeditor.player.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Brush {
    // Together, these two parameters serve as the ID for the brush
    public UUID owner; // Different people can have different brushes
    public ItemStack item; // Each person can have a different brush for different items

    // Variables the brush needs
    BrushShape shapeGenerator;
    List<Operator> operations;

    // Static player
    public static Player currentPlayer = null;

    // Store brushes
    public static final Map<String, BrushShape> brushShapes = new HashMap<>();

    public static boolean removeBrush(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        Brush br = null;

        PlayerWrapper playerWrapper = PlayerManager.INSTANCE.getPlayerWrapper(player);
        for (Brush b : playerWrapper.getBrushes()) {
            if (b.item.equals(item)) {
                br = b;
            }
        }
        if (br != null) {
            playerWrapper.getBrushes().remove(br);
        }
        return true;
    }

    public Brush(String[] brushOperation, int brushOpOffset, Player player) {
        try {
            currentPlayer = player;

            ItemStack brushItem = player.getInventory().getItemInMainHand();

            // Make sure this brush doesn't already exist. If it does, remove it
            removeBrush(player);

            // Create a brush, and assign the easy variables to it
            owner = player.getUniqueId();
            item = brushItem;

            brushOpOffset += 2; // Used to remove brush parameters from the operation

            // Get the shape generator, and store the args
            shapeGenerator = brushShapes.get(brushOperation[1]).getClass().getDeclaredConstructor().newInstance();
            Main.logDebug("Brush type: " + shapeGenerator.getClass().getSimpleName());
            try {
                do {
                    if (brushOpOffset >= brushOperation.length
                            || brushOperation[brushOpOffset].equalsIgnoreCase("end")) {
                        brushOpOffset += 2;
                        break;
                    }
                    shapeGenerator.addNewArgument(brushOperation[brushOpOffset]);
                    Main.logDebug("Passed arg \"" + brushOperation[brushOpOffset] + "\", processed="
                            + shapeGenerator.lastInputProcessed());
                    brushOpOffset++;
                }
                while (shapeGenerator.lastInputProcessed());
                brushOpOffset--;
            } catch (Exception e) {
                Main.logError(
                        "Could not parse brush arguments. Please check that you provided enough numerical arguments for the brush shape.",
                        player, e);
                return;
            }

            if (shapeGenerator.gotEnoughArgs()) {
                Main.logError("Not enough inputs to the brush shape were provided. Please provide enough inputs.",
                        player, null);
            }

            List<String> opArray = new LinkedList<>(Arrays.asList(brushOperation));
            operations = new ArrayList<>();
            for (int i = 0; i < shapeGenerator.operatorCount(); i++) {
                // Construct the operator
                // Start by removing brush parameters or old operator arguments
                while (brushOpOffset > 0) {
                    opArray.remove(0);
                    brushOpOffset--;
                }
                // Construct the string
                String opStr = "";
                for (String s : opArray) {
                    opStr = opStr.concat(s).concat(" ");
                }
                // And then construct the operator
                Operator newOperator = new Operator(opStr, player);
                operations.add(newOperator);

                // And update the number of args to remove
                brushOpOffset = newOperator.getEntryNode().consumedArgs;
            }

            // Store the brush and return success
            PlayerWrapper playerWrapper = PlayerManager.INSTANCE.getPlayerWrapper(player);
            playerWrapper.getBrushes().add(this);
            player.sendMessage("§dBrush created and bound to item in hand.");

        } catch (Exception e) {
            Main.logError("Error creating brush. Please check your syntax.", player, e);
        }
    }

    public static BrushShape GetBrushShape(String name) {
        if (brushShapes.containsKey(name)) {
            try {
                return brushShapes.get(name).getClass().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                Main.logDebug("Error instantiating brush.");
                return null;
            }
        }
        return null;
    }

    public static void AddBrushShape(String name, BrushShape shape) {
        if (brushShapes.containsKey(name)) {
            return;
        }
        brushShapes.put(name, shape);
    }

    public void operate(double x, double y, double z) {
        try {
            currentPlayer = getOwner();
            shapeGenerator.runBrush(operations, x, y, z, currentPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            Main.logError("Error operating with brush. Please check your syntax.", getOwner(), e);
        }
    }

    public Player getOwner() {
        return Bukkit.getServer().getPlayer(owner);
    }
}
