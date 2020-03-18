package com.fourteener.worldeditor.scripts.bundled.quickbrush;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.scripts.Craftscript;

public class ScriptBallBrushSet extends Craftscript {

	@Override
	public Set<BlockState> perform(LinkedList<String> args, Player player, String label) {
		String brushRadius = args.get(0);
		String blocksToSet = args.get(1);
		String[] individualBlocks = blocksToSet.split(",");
		String opToRun = "";
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
			opToRun = opToRun.concat("false");
		}
		// Perform the set command
		player.sendMessage("BRUSH MADE");
		player.performCommand("fx br s " + brushRadius + " 0.5 " + opToRun);
		return null;
	}
}
