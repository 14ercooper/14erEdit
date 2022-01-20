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

package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OddsNodeTest extends NodeTestCase {
    @Test
    public void testParseOddsNode() {
        Node node = parseFromString("% 50");

        assertTrue(node instanceof OddsNode);
        assertNotNull(((OddsNode) node).arg);
        assertEquals(50.0, ((OddsNode) node).arg.getValue(dummyState));
    }

    @Test
    public void testPerformOddsNode() {
        Node node = parseFromString("% 50");

        Main.getRand().setSeed(14);

        assertFalse(node.performNode(dummyState));
        assertFalse(node.performNode(dummyState));
        assertTrue(node.performNode(dummyState));
        assertTrue(node.performNode(dummyState));
        assertTrue(node.performNode(dummyState));
    }

    @Test
    public void testPerformOddsNodeNoSeed() {
        Node node = parseFromString("% 50");
        int trueCount = 0;
        for (int i = 0; i < 1000000; i++) {
            if (node.performNode(dummyState)) {
                trueCount++;
            }
        }
        double empiricalOdds = (double) trueCount / 1000000.0;
        double distFrom50 = Math.abs(empiricalOdds - 0.5);

        assertTrue(distFrom50 < 0.01);
    }

    @Test
    public void testCorrectArgCount() {
        OddsNode oddsNode = new OddsNode();

        assertEquals(1, oddsNode.getArgCount());
    }
}