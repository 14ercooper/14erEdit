package com._14ercooper.worldeditor.blockiterator.iterators;

import java.util.List;

import org.bukkit.block.Block;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

// This is an annoying class
public class CubeIterator extends BlockIterator {

	int x1, y1, z1;
	int x2, y2, z2;
	long totalBlocks;
	long doneBlocks = 0;
	int x, y, z;
	int xStep = 1, yStep = 1, zStep = 1;
	int executionOrder = 0;

	@Override
	public CubeIterator newIterator(List<String> args) {
		try {
			CubeIterator iterator = new CubeIterator();
			iterator.x1 = Integer.parseInt(args.get(0));
			iterator.y1 = Integer.parseInt(args.get(1));
			iterator.z1 = Integer.parseInt(args.get(2));
			iterator.x2 = Integer.parseInt(args.get(3));
			iterator.y2 = Integer.parseInt(args.get(4));
			iterator.z2 = Integer.parseInt(args.get(5));
			if (args.size() > 6) {
				iterator.executionOrder = Integer.parseInt(args.get(6));
				if (iterator.executionOrder < 0) iterator.executionOrder = 0;
				if (iterator.executionOrder > 5) iterator.executionOrder = 0;
			}
			if (iterator.x2 < iterator.x1) {
				iterator.xStep = -1;
			}
			if (iterator.y2 < iterator.y1) {
				iterator.yStep = -1;
			}
			if (iterator.z2 < iterator.z1) {
				iterator.zStep = -1;
			}
			int dx = Math.abs(iterator.x2 - iterator.x1) + 1;
			int dy = Math.abs(iterator.y2 - iterator.y1) + 1;
			int dz = Math.abs(iterator.z2 - iterator.z1) + 1;
			iterator.totalBlocks = dx * dy * dz;
			iterator.x = iterator.x1;
			iterator.y = iterator.y1;
			iterator.z = iterator.z1;
			if (iterator.executionOrder == 0 || iterator.executionOrder == 1) iterator.x -= iterator.xStep;
			if (iterator.executionOrder == 2 || iterator.executionOrder == 4) iterator.z -= iterator.zStep;
			if (iterator.executionOrder == 3 || iterator.executionOrder == 5) iterator.y -= iterator.yStep;
			Main.logDebug("From " + iterator.x1 + "," + iterator.y1 + "," + iterator.z1 + " to " + iterator.x2 + "," + iterator.y2 + "," + iterator.z2);
			Main.logDebug("Starting block: " + iterator.x + "," + iterator.y + "," + iterator.z);
			Main.logDebug("Steps: " + iterator.xStep + "," + iterator.yStep + "," + iterator.zStep);
			Main.logDebug("Cube iterator execution order: " + iterator.executionOrder);
			return iterator;
		} catch (Exception e) {
			Main.logError("Could not create cube iterator. Please check your brush parameters/if you have a selection box.", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public Block getNext() {
		if (executionOrder == 0) { // xzy
			x += xStep;
			doneBlocks++;
			if (!inRange(x, x1, x2)) {
				z += zStep;
				x = x1;
			}
			if (!inRange(z, z1, z2)) {
				y += yStep;
				z = z1;
			}
			if (!inRange(y, y1, y2)) {
				return null;
			}
		}
		if (executionOrder == 1) { // xyz
			x += xStep;
			doneBlocks++;
			if (!inRange(x, x1, x2)) {
				y += yStep;
				x = x1;
			}
			if (!inRange(y, y1, y2)) {
				z += zStep;
				y = y1;
			}
			if (!inRange(z, z1, z2)) {
				return null;
			}
		}
		if (executionOrder == 2) { // zxy
			z += zStep;
			doneBlocks++;
			if (!inRange(z, z1, z2)) {
				x += xStep;
				z = z1;
			}
			if (!inRange(x, x1, x2)) {
				y += yStep;
				x = x1;
			}
			if (!inRange(y, y1, y2)) {
				return null;
			}
		}
		if (executionOrder == 3) { // yxz
			y += yStep;
			doneBlocks++;
			if (!inRange(y, y1, y2)) {
				x += xStep;
				y = y1;
			}
			if (!inRange(x, x1, x2)) {
				z += zStep;
				x = x1;
			}
			if (!inRange(z, z1, z2)) {
				return null;
			}
		}
		if (executionOrder == 4) { // zyx
			z += zStep;
			doneBlocks++;
			if (!inRange(z, z1, z2)) {
				y += yStep;
				z = z1;
			}
			if (!inRange(y, y1, y2)) {
				x += xStep;
				y = y1;
			}
			if (!inRange(x, x1, x2)) {
				return null;
			}
		}
		if (executionOrder == 5) { // yzx
			y += yStep;
			doneBlocks++;
			if (!inRange(y, y1, y2)) {
				z += zStep;
				y = y1;
			}
			if (!inRange(z, z1, z2)) {
				x += xStep;
				z = z1;
			}
			if (!inRange(x, x1, x2)) {
				return null;
			}
		}

		return Operator.currentPlayer.getWorld().getBlockAt(x, y, z);
	}

	@Override
	public long getTotalBlocks() {
		return totalBlocks;
	}

	@Override
	public long getRemainingBlocks() {
		return totalBlocks - doneBlocks;
	}

	private boolean inRange(int val, int r1, int r2) {
		int min = 0;
		int max = 0;
		if (r1 <= r2) {
			min = r1;
			max = r2;
		}
		else {
			min = r2;
			max = r1;
		}
		return (val >= min && val <= max);
	}
}
