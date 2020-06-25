package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.MultiIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class RandomSplatter extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		try {
			int splatterRadiusMin = (int) (double) args.get(0);
			int splatterRadiusMax = (int) (double) args.get(1);
			int sphereCountMin = (int) (double) args.get(2);
			int sphereCountMax = (int) (double) args.get(3);
			int sphereRadiusMin = (int) (double) args.get(4);
			int sphereRadiusMax = (int) (double) args.get(5);
			double radiusCorrection = args.get(6);
			int spheresGenerated = 0;
			Set<BlockIterator> spheres = new HashSet<BlockIterator>();
			Random rand = new Random();
			int sphereCount = rand.nextInt(sphereCountMax - sphereCountMin) + sphereCountMin;
			while (spheresGenerated < sphereCount) {
				int splatterRadius = rand.nextInt(splatterRadiusMax - splatterRadiusMin) + splatterRadiusMin;
				double xOff = rand.nextInt(splatterRadius) * (rand.nextInt(2) == 0 ? -1 : 1);
				double yOff = rand.nextInt(splatterRadius) * (rand.nextInt(2) == 0 ? -1 : 1);
				double zOff = rand.nextInt(splatterRadius) * (rand.nextInt(2) == 0 ? -1 : 1);
				if (xOff*xOff + yOff*yOff + zOff*zOff < splatterRadius*splatterRadius + 0.5) {
					int sphereRadius = rand.nextInt(sphereRadiusMax - sphereRadiusMin) + sphereRadiusMin;
					List<String> argList = new ArrayList<String>();
					argList.add(Integer.toString((int) (x + xOff)));
					argList.add(Integer.toString((int) (y + yOff)));
					argList.add(Integer.toString((int) (z + zOff)));
					argList.add(Integer.toString(sphereRadius));
					argList.add(Integer.toString(0));
					argList.add(Double.toString(radiusCorrection));
					spheres.add(GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList));
					spheresGenerated++;
				}

			}
			return ((MultiIterator) GlobalVars.iteratorManager.getIterator("multi")).newIterator(spheres);
		} catch (Exception e) {
			Main.logError("Could not parse random splatter brush. Did you provide splatter radius min&max, sphere count min&max, sphere radius min&max, and radius correction?", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public double GetArgCount() {
		// Splatter radius min & max, sphere count min & max, sphere radius min & max, radius correction
		return 7;
	}

}
