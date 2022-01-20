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
import com._14ercooper.worldeditor.operations.operators.core.XNode;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitNodeTest extends NodeTestCase {
    @Test
    public void testParseLimitNode() {
        Node node = parseFromString("limit 5 7");
        assertTrue(node instanceof LimitNode);
        assertNotNull(((LimitNode) node).arg1);
        assertNotNull(((LimitNode) node).arg2);

        node = parseFromString("limit x 7");
        assertTrue(node instanceof LimitNode);
        assertTrue(((LimitNode) node).arg1 instanceof XNode);
        assertNotNull(((LimitNode) node).arg2);
    }

    @Test
    public void testPerformLimitNode() {
        LimitNode node = (LimitNode) parseFromString("limit 3 5");
        assertTrue(node.performNode(dummyState));

        node = (LimitNode) parseFromString("limit 7 5");
        assertFalse(node.performNode(dummyState));
    }

    @Test
    public void testCorrectArgCount() {
        LimitNode limitNode = new LimitNode();
        assertEquals(2, limitNode.getArgCount());
    }
}