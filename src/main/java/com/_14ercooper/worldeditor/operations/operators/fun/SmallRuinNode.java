package com._14ercooper.worldeditor.operations.operators.fun;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class SmallRuinNode extends Node {

    Node block;
    RangeNode stackSize;
    RangeNode xMax, zMax;

    @Override
    public SmallRuinNode newNode() {
	SmallRuinNode node = new SmallRuinNode();
	node.xMax = GlobalVars.operationParser.parseRangeNode();
	node.zMax = GlobalVars.operationParser.parseRangeNode();
	node.stackSize = GlobalVars.operationParser.parseRangeNode();
	node.block = GlobalVars.operationParser.parsePart();
	return node;
    }

    @Override
    public boolean performNode() {
        int xSize = Main.randRange((int) xMax.getMin(), (int) xMax.getMax());
        int zSize = Main.randRange((int) zMax.getMin(), (int) zMax.getMax());

	int stackCount = Main.randRange((int) stackSize.getMin(), (int) stackSize.getMax());

        Block savedBlock = Operator.currentBlock;

	for (int ruinNum = 0; ruinNum < stackCount; ruinNum++) {
	    Block currBlock = savedBlock.getRelative(BlockFace.UP, 4 * ruinNum);

	    // Create base including fill
	    for (int xO = -xSize; xO <= xSize; xO++) {
		for (int zO = -zSize; zO <= zSize; zO++) {
		    Block currBlockOffset = currBlock.getRelative(xO, 0, zO);
		    // Set block
		    if (currBlockOffset.getType() == Material.AIR) {
			Operator.currentBlock = currBlockOffset;
			block.performNode();
		    }
		    else {
			if (GlobalVars.rand.nextBoolean()) {
			    Operator.currentBlock = currBlockOffset;
			    block.performNode();
			}
		    }

		    // Do base fill on bottom layer - 1 indexed due to initial offset
		    if (ruinNum == 0) {
			for (int i = 1; i < 4; i++) {
			    Operator.currentBlock = currBlockOffset.getRelative(BlockFace.DOWN, i);
			    if (Operator.currentBlock.getType() == Material.AIR)
				i--;
			    block.performNode();
			}
		    }
		}
	    }

	    // Create walls
	    for (int xO = -xSize; xO <= xSize; xO++) {
		for (int zO = -zSize; zO <= zSize; zO++) {
		    if (xO == -xSize || xO == xSize || zO == -zSize || zO == zSize) {
			for (int i = 0; i < 5; i++) {
			    if (GlobalVars.rand.nextInt(5) == 0)
				break;
			    Operator.currentBlock = currBlock.getRelative(xO, i, zO);
			    block.performNode();
			}
		    }
		}
	    }
	}

	// Clean up and return
	Operator.currentBlock = savedBlock;
	return true;
    }

    @Override
    public int getArgCount() {
	return 3;
    }

}
