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

import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EveryXNodeTest extends NodeTestCase {
    @Test
    public void testParseEveryXNode() {
        Node node = parseFromString(";; x 3");
        assertTrue(node instanceof EveryXNode);
        assertEquals(0, ((EveryXNode) node).arg1);
        assertNotNull(((EveryXNode) node).arg2);

        node = parseFromString(";; y 3");
        assertTrue(node instanceof EveryXNode);
        assertEquals(1, ((EveryXNode) node).arg1);
        assertNotNull(((EveryXNode) node).arg2);

        node = parseFromString(";; z 3");
        assertTrue(node instanceof EveryXNode);
        assertEquals(2, ((EveryXNode) node).arg1);
        assertNotNull(((EveryXNode) node).arg2);
    }

    @Test
    public void testPerformEveryXNode() {
        EveryXNode node = (EveryXNode) parseFromString(";; x 2");
        boolean result = node.performNode(dummyState);
        assertTrue(result);

        node = (EveryXNode) parseFromString(";; x 3");
        result = node.performNode(dummyState);
        assertFalse(result);
    }

    @Test
    public void testCorrectArgCount() {
        EveryXNode everyXNode = new EveryXNode();
        assertEquals(2, everyXNode.getArgCount());
    }
}