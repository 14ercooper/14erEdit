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

package com._14ercooper.worldeditor.operations.operators.math;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

import java.util.Random;

public class RandomNode extends NumberNode {
    // Returns a new node
    @Override
    public RandomNode newNode(ParserState parserState) {
        return new RandomNode();
    }

    // Return the number
    @Override
    public double getValue(OperatorState state) {
        double distX = state.getCurrentBlock().x - state.getOriginLocation().getBlockX();
        double distY = state.getCurrentBlock().y - state.getOriginLocation().getBlockY();
        double distZ = state.getCurrentBlock().z - state.getOriginLocation().getBlockZ();
        Random random = new Random((long) ((distX / 20.0) * (distY / 20.0) * (distZ / 20.0)));
        random.nextDouble();
        return random.nextDouble();
    }

    @Override
    public int getInt(OperatorState state) {
        return (int) Math.round(getValue(state));
    }

    // Get how many arguments this type of node takes
    @Override
    public int getArgCount() {
        return 0;
    }
}
