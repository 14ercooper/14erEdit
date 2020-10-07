package com._14ercooper.worldeditor.brush.shapes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class Multi extends BrushShape {

    String file = "";
    List<String> args = new ArrayList<String>();
    List<BlockIterator> iters = null;
    List<Operator> ops = null;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	Main.logError("Multibrush used in a normal brush context. This is an error.", Operator.currentPlayer);
	return null;
    }

    @Override
    public void addNewArgument(String argument) {
	if (file.isEmpty()) {
	    file = argument;
	}
	else {
	    args.add(argument);
	}
    }

    @Override
    public boolean lastInputProcessed() {
	return true;
    }

    @Override
    public boolean gotEnoughArgs() {
	return !file.isEmpty();
    }

    public List<BlockIterator> getIters(double x, double y, double z) {
	genMultibrush(x, y, z);
	return iters;
    }

    public List<Operator> getOps(double x, double y, double z) {
	return ops;
    }

    @SuppressWarnings("unused")
    private void genMultibrush(double x, double y, double z) {
	// Create the lists
	iters = new ArrayList<BlockIterator>();
	ops = new ArrayList<Operator>();
	
	// Get the file name
	String filename = "plugins/14erEdit/multibrushes/" + file;
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
	List<String> brushesRaw = new ArrayList<String>();
	try {
	    brushesRaw = Files.readAllLines(Paths.get(filename));
	}
	catch (IOException e1) {
	    Main.logError("Error loading multibrush file: " + filename, Brush.currentPlayer);
	}

	// Perform templating
	List<String> brushes = new LinkedList<String>();
	for (String s : brushesRaw) {
	    if (!s.isEmpty()) {
		for (int i = args.size(); i > 0; i--) {
		    s = s.replaceAll("\\$" + (i), args.get(i - 1));
		}
		brushes.add(s);
	    }
	}

	// Parse each brush shape and operator
	for (String s : brushes) {
	    String[] rawData = s.split(" ");
	    int brushOpOffset = 1; // Used to remove brush parameters from the operation

	    // Get the shape generator, and store the args
	    BrushShape shapeGenerator = Brush.GetBrushShape(rawData[0]);
	    try {
		do {
		    if (rawData[brushOpOffset].equalsIgnoreCase("end")) {
			brushOpOffset += 2;
			break;
		    }
		    shapeGenerator.addNewArgument(rawData[brushOpOffset]);
		    brushOpOffset++;
		}
		while (shapeGenerator.lastInputProcessed());
		brushOpOffset--;
	    }
	    catch (Exception e) {
		Main.logError(
			"Could not parse brush arguments. Please check that you provided enough numerical arguments for the brush shape.",
			Brush.currentPlayer);
		return;
	    }

	    if (!shapeGenerator.gotEnoughArgs()) {
		Main.logError("Not enough inputs to the brush shape were provided. Please provide enough inputs.",
			Brush.currentPlayer);
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
	    Operator operation = new Operator(opStr, Brush.currentPlayer);

	    // Invalid operator?
	    if (operation == null)
		continue;

	    // Add to the lists
	    iters.add(shapeGenerator.GetBlocks(x, y, z));
	    ops.add(operation);
	}
    }
}

//else {
//	// Mark as multibrush
//	multibrush = true;
//
//	// Get the file name
//	String filename = "plugins/14erEdit/multibrushes/" + brushOperation[2];
//	if (Files.exists(Paths.get(filename))) {
//	    // We're already good
//	}
//	else if (Files.exists(Paths.get(filename + ".mb"))) {
//	    filename += ".mb";
//	}
//	else if (Files.exists(Paths.get(filename + ".txt"))) {
//	    filename += ".txt";
//	}
//
//	// Load the set of brushes from the file
//	List<String> brushesRaw = Files.readAllLines(Paths.get(filename));
//
//	// Perform templating
//	List<String> brushes = new LinkedList<String>();
//	for (String s : brushesRaw) {
//	    if (!s.isEmpty()) {
//		for (int i = brushOperation.length - 1; i >= 2; i--) {
//		    s = s.replaceAll("\\$" + (i - 2), brushOperation[i]);
//		}
//		brushes.add(s);
//	    }
//	}
//
//	// Parse each brush shape and operator
//	for (String s : brushes) {
//	    String[] rawData = s.split(" ");
//	    brushOpOffset = 1; // Used to remove brush parameters from the operation
//
//	    // Get the shape generator, and store the args
//	    shapeGenerator = brushShapes.get(rawData[0]);
//	    LinkedList<Double> thisShapeArgs = new LinkedList<Double>();
//	    try {
//		for (int i = 0; i < shapeGenerator.GetArgCount(); i++) {
//		    thisShapeArgs.add(Double.parseDouble(rawData[1 + i]));
//		    brushOpOffset++;
//		}
//	    }
//	    catch (Exception e) {
//		Main.logError(
//			"Could not parse brush arguments. Please check that you provided enough numerical arguments for the brush shape.",
//			player);
//		return;
//	    }
//
//	    // Construct the operator
//	    // Start by removing brush parameters
//	    List<String> opArray = new LinkedList<String>(Arrays.asList(rawData));
//	    while (brushOpOffset > 0) {
//		opArray.remove(0);
//		brushOpOffset--;
//	    }
//	    // Construct the string
//	    String opStr = "";
//	    for (String str : opArray) {
//		opStr = opStr.concat(str).concat(" ");
//	    }
//	    // And then construct the operator
//	    operation = new Operator(opStr, player);
//
//	    // Invalid operator?
//	    if (operation == null)
//		continue;
//
//	    // Add to the lists
//	    shapeGenerators.add(shapeGenerator);
//	    shapeArgList.add(thisShapeArgs);
//	    operations.add(operation);
//	}
//
//	// Save the brush
//	BrushListener.brushes.add(this);
//	player.sendMessage(
//		"§aNOTE: Multibrushes run in large edit mode, so no undo will be registered. Please be careful.");
//	player.sendMessage("§dBrush created and bound to item in hand.");
//	GlobalVars.errorLogged = false;
//}
