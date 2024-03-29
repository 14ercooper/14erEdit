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

package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.function.NoiseNode;

public class NumberNode extends Node {

    // Stores this node's argument
    public double arg = 0;

    // This was a randrange
    boolean isRange = false;
    NumberNode rangeMin;
    NumberNode rangeMax;

    // This was a noiserange
    boolean isNoise = false;
    NoiseNode noise;

    // Is absolute
    public boolean isAbsolute = false;

    // Create a new number node
    @Override
    public NumberNode newNode(ParserState parserState) {
        NumberNode node = new NumberNode();
        parserState.setIndex(parserState.getIndex() - 1);
        String num = "undefined";
        try {
            num = Parser.parseStringNode(parserState).contents;

            if (num.equalsIgnoreCase("%-") || num.equalsIgnoreCase("randrange")) {
                node.rangeMin = Parser.parseNumberNode(parserState);
                node.rangeMax = Parser.parseNumberNode(parserState);
                node.isRange = true;

                return node;
            }

            if (num.equalsIgnoreCase("#-") || num.equalsIgnoreCase("randnoise")) {
                node.rangeMin = Parser.parseNumberNode(parserState);
                node.rangeMax = Parser.parseNumberNode(parserState);
                node.isNoise = true;

                return node;
            }

            if (num.toLowerCase().contains("a")) {
                node.isAbsolute = true;
                node.arg = Double.parseDouble(num.replaceAll("[A-Za-z]+", ""));
                return node;
            }

            node.arg = Double.parseDouble(num);
            return node;
        } catch (Exception e) {
            Main.logError("Could not parse number node. " + num + " is not a number.", parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return !(Math.abs(arg) < 0.01);
    }

    // Return the number
    public double getValue(OperatorState state) {
        return getValue(state, 0);
    }

    public double getValue(OperatorState state, double center) {
        if (isRange) {
            double rangeMinVal = rangeMin.getValue(state);
            double rangeMaxVal = rangeMax.getValue(state);
            return (Main.getRand().nextDouble() * (rangeMaxVal - rangeMinVal)) + rangeMinVal
                    + center;
        } else if (isNoise) {
            return (noise.getNum(state)) + center;
        } else {
            return arg;
        }
    }

    // Return the number as an int
    public int getInt(OperatorState state) {
        return getInt(state, 0);
    }

    public int getInt(OperatorState state, int center) {
        return (int) getValue(state, center);
    }

    public int getMaxInt(OperatorState state) {
        if (isRange || isNoise) {
            return (int) rangeMax.getValue(state);
        } else {
            return (int) arg;
        }
    }

    // Get how many arguments this type of node takes
    @Override
    public int getArgCount() {
        return 1;
    }

}
