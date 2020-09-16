package com._14ercooper.worldeditor.brush;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;

public class Brush {
    // Together, these two parameters serve as the ID for the brush
    public Player owner; // Different people can have different brushes
    public ItemStack item; // Each person can have a different brush for different items

    // Variables the brush needs
    BrushShape shapeGenerator;
    List<Double> shapeArgs = new LinkedList<Double>();
    Operator operation;

    // Multibrush
    boolean multibrush = false;
    List<BrushShape> shapeGenerators = new LinkedList<BrushShape>();
    List<LinkedList<Double>> shapeArgList = new LinkedList<LinkedList<Double>>();
    List<Operator> operations = new LinkedList<Operator>();

    // Store brushes
    static Map<String, BrushShape> brushShapes = new HashMap<String, BrushShape>();

    public static boolean removeBrush(Player player) {
	ItemStack item = player.getInventory().getItemInMainHand();

	Brush br = null;

	for (Brush b : BrushListener.brushes) {
	    if (b.owner.equals(player) && b.item.equals(item)) {
		br = b;
	    }
	}
	if (br != null) {
	    BrushListener.brushes.remove(br);
	}
	return true;
    }

    public static boolean AddBrushShape(String name, BrushShape shape) {
	if (brushShapes.containsKey(name)) {
	    return false;
	}
	brushShapes.put(name, shape);
	return true;
    }

    public static BrushShape GetBrushShape(String name) {
	if (brushShapes.containsKey(name)) {
	    return brushShapes.get(name);
	}
	return null;
    }

    public Brush(String brushShape, String brushRadius, String[] brushOperation, int brushOpOffset, Player player) {
	try {

	    ItemStack brushItem = player.getInventory().getItemInMainHand();

	    // Make sure this brush doesn't already exist. If it does, remove it
	    removeBrush(player);

	    // Create a brush, and assign the easy variables to it
	    owner = player;
	    item = brushItem;

	    if (!brushOperation[1].equalsIgnoreCase("multi")) {
		brushOpOffset += 2; // Used to remove brush parameters from the operation

		// Get the shape generator, and store the args
		shapeGenerator = brushShapes.get(brushOperation[1]);
		try {
		    for (int i = 0; i < shapeGenerator.GetArgCount(); i++) {
			shapeArgs.add(Double.parseDouble(brushOperation[2 + i]));
			brushOpOffset++;
		    }
		}
		catch (Exception e) {
		    Main.logError(
			    "Could not parse brush arguments. Please check that you provided enough numerical arguments for the brush shape.",
			    player);
		    return;
		}

		// Construct the operator
		// Start by removing brush parameters
		List<String> opArray = new LinkedList<String>(Arrays.asList(brushOperation));
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
		operation = new Operator(opStr, player);

		// Invalid operator?
		if (operation == null)
		    return;

		// Store the brush and return success
		BrushListener.brushes.add(this);
		player.sendMessage("§dBrush created and bound to item in hand.");
		GlobalVars.errorLogged = false;
	    }
	    else {
		// Mark as multibrush
		multibrush = true;
		
		// Get the file name
		String filename = "plugins/14erEdit/multibrushes/" + brushOperation[2];
		if (Files.exists(Paths.get(filename))) {
		    // We're already good
		}
		else if (Files.exists(Paths.get(filename + ".mb"))) {
		    filename += ".mb";
		}
		else if (Files.exists(Paths.get(filename + ".txt"))) {
		    filename += ".txt";
		}

		// Load the set of brushes from the file
		List<String> brushesRaw = Files.readAllLines(Paths.get(filename));

		// Perform templating
		List<String> brushes = new LinkedList<String>();
		for (String s : brushesRaw) {
		    if (!s.isEmpty()) {
			for (int i = brushOperation.length - 1; i >= 2; i--) {
			    s = s.replaceAll("\\$" + (i - 2), brushOperation[i]);
			}
			brushes.add(s);
		    }
		}

		// Parse each brush shape and operator
		for (String s : brushes) {
		    String[] rawData = s.split(" ");
		    brushOpOffset = 1; // Used to remove brush parameters from the operation

		    // Get the shape generator, and store the args
		    shapeGenerator = brushShapes.get(rawData[0]);
		    LinkedList<Double> thisShapeArgs = new LinkedList<Double>();
		    try {
			for (int i = 0; i < shapeGenerator.GetArgCount(); i++) {
			    thisShapeArgs.add(Double.parseDouble(rawData[1 + i]));
			    brushOpOffset++;
			}
		    }
		    catch (Exception e) {
			Main.logError(
				"Could not parse brush arguments. Please check that you provided enough numerical arguments for the brush shape.",
				player);
			return;
		    }

		    // Construct the operator
		    // Start by removing brush parameters
		    List<String> opArray = new LinkedList<String>(Arrays.asList(rawData));
		    while (brushOpOffset > 0) {
			opArray.remove(0);
			brushOpOffset--;
		    }
		    // Construct the string
		    String opStr = "";
		    for (String str : opArray) {
			opStr = opStr.concat(str).concat(" ");
		    }
		    // And then construct the operator
		    operation = new Operator(opStr, player);

		    // Invalid operator?
		    if (operation == null)
			continue;
		    
		    // Add to the lists
		    shapeGenerators.add(shapeGenerator);
		    shapeArgList.add(thisShapeArgs);
		    operations.add(operation);
		}

		// Save the brush
		BrushListener.brushes.add(this);
		player.sendMessage("§dBrush created and bound to item in hand.");
		GlobalVars.errorLogged = false;
	    }
	}
	catch (Exception e) {
	    Main.logError("Error creating brush. Please check your syntax.", player);
	}
    }

    @SuppressWarnings("static-access")
    public boolean operate(double x, double y, double z) {
	try {
	    if (!multibrush) {
		// Build an array of all blocks to operate on
		BlockIterator blockArray = shapeGenerator.GetBlocks(shapeArgs, x, y, z);

		if (blockArray.getTotalBlocks() == 0) {
		    return false;
		}
		Main.logDebug("Block array size is " + Long.toString(blockArray.getTotalBlocks())); // -----

		GlobalVars.asyncManager.scheduleEdit(operation, owner, blockArray);

		return true;
	    }
	    else {
		// Create a multi-operator async chain
		List<BlockIterator> iters = new LinkedList<BlockIterator>();
		List<Operator> ops = new LinkedList<Operator>();
		ops.addAll(operations);
		for (int i = 0; i < shapeGenerators.size(); i++) {
		    iters.add(shapeGenerators.get(i).GetBlocks(shapeArgList.get(i), x, y, z));
		}
		
		GlobalVars.asyncManager.scheduleEdit(iters, ops, owner);
		
		return true;
	    }
	}
	catch (Exception e) {
	    Main.logError("Error operating with brush. Please check your syntax.", owner);
	    return false;
	}
    }
}
