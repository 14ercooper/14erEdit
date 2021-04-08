package com._14ercooper.worldeditor.scripts.bundled.selection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;

public class ScriptGrass extends Craftscript {
    @Override
    public boolean perform(LinkedList<String> args, Player player, String label) {
	try {
	    String opToRun = "";
	    // Check for missing args to fill in with "default" brush
	    // Example brush: /fx sel op ? air ? _ - 1 1 ~ air ? ^ - 1 5 air ? % 50 ? % 80 >
	    // grass ? % 50 > tall_grass ? % 50 > poppy > dandelion false false false false
	    if (args.size() < 3) {
		double density = 0.5;
		double blocksAbove = 5;

		for (String s : args) {
		    try {
			if (Double.parseDouble(s) >= 1.0) {
			    blocksAbove = (int) Math.round(Double.parseDouble(s));
			}
			else {
			    density = Double.parseDouble(s);
			}
		    }
		    catch (Exception e) {
		    }
		}
		player.performCommand("fx sel op ? air ? _ - 1 1 ~ air ? ^ - 1 " + blocksAbove + " air ? % "
			+ (density * 100.0)
			+ " ? % 80 > grass ? % 50 > tall_grass ? % 50 > poppy > dandelion false false false false");
		player.performCommand(
			"fx sel op ? air ? _ - 1 1 minecraft:tall_grass[half=lower] >> minecraft:tall_grass[half=upper] false false");
		return true;
	    }

	    // [AirSpaces]
	    int airSpaces = Integer.parseInt(args.get(1));
	    opToRun = opToRun.concat("? ^ - 1 " + airSpaces + " air ");

	    // [Density]
	    double density = Double.parseDouble(args.get(2));
	    opToRun = opToRun.concat("? % " + (density * 100.0) + " ");

	    // Spot below is solid?
	    opToRun = opToRun.concat("? _ - 1 1 ~ air ");

	    // [Mixture]
	    String blocksToSet = args.get(0);
	    String[] individualBlocks = blocksToSet.split(",");
	    if (individualBlocks.length == 1) {
		// Parse if it needs block data
		if (individualBlocks[0].contains("[")) {
		    opToRun = ">> ";
		    if (individualBlocks[0].contains("mineacraft:")) {
			opToRun = opToRun.concat(individualBlocks[0]);
		    }
		    else {
			opToRun = opToRun.concat("minecraft:");
			opToRun = opToRun.concat(individualBlocks[0]);
		    }
		}
		// Parse if no block data
		else {
		    opToRun = "> " + individualBlocks[0];
		}
	    }
	    else {
		// First calculate the odds
		List<Double> oddsList = new ArrayList<Double>();
		double oddsRemaining = 1.0;
		for (String s : individualBlocks) {
		    Double oddsToUse = Double.parseDouble(s.split("%")[0]);
		    oddsList.add(oddsToUse / oddsRemaining);
		    oddsRemaining -= (oddsToUse / 100.0);
		}
		// Then construct the operation
		for (int i = 0; i < individualBlocks.length; i++) {
		    opToRun = opToRun.concat("? % " + oddsList.get(i) + " ");
		    String parsedBlock = "", parseText = individualBlocks[i].split("%")[1];
		    // Parse if it needs block data
		    if (parseText.contains("[")) {
			parsedBlock = ">> ";
			if (parseText.contains("mineacraft:")) {
			    parsedBlock = parsedBlock.concat(parseText);
			}
			else {
			    parsedBlock = parsedBlock.concat("minecraft:");
			    parsedBlock = parsedBlock.concat(parseText);
			}
		    }
		    // Parse if no block data
		    else {
			parsedBlock = "> " + parseText;
		    }
		    // Add to the operator
		    opToRun = opToRun.concat(parsedBlock + " ");
		}
		opToRun = opToRun.concat("false ");
	    }
	    // Closing for below block
	    opToRun = opToRun.concat("false ");
	    // Closing false statement for [density] IF node
	    opToRun = opToRun.concat("false ");
	    // Closing false statement for [airSpaces] IF node
	    opToRun = opToRun.concat("false");

	    // Perform the set command
	    player.sendRawMessage("fx sel op " + opToRun);
	    player.performCommand("fx sel op " + opToRun);
	    return true;
	}
	catch (Exception e) {
	    Main.logError("Error performing grass script. Did you pass in the correct parameters?",
		    Operator.currentPlayer, e);
	    return false;
	}
    }
}
