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

class FunctionNodeTest extends NodeTestCase {
    @Test
    public void testParseFunctionNode() {
        Node node = parseFromString("fx testFunction 0 notAnArg");
        assertTrue(node instanceof FunctionNode);
        assertEquals("testFunction", ((FunctionNode) node).filename);
        assertEquals(0, ((FunctionNode) node).args.size());
    }
    @Test
    public void testParseFunctionNodeArgs() {
        Node node = parseFromString("fx testFunction 2 arg1 arg2 notAnArg");
        assertTrue(node instanceof FunctionNode);
        assertEquals("testFunction", ((FunctionNode) node).filename);
        assertEquals(2, ((FunctionNode) node).args.size());
    }

    @Test
    public void testPerformFunctionNode() {
        FunctionNode node = (FunctionNode) parseFromString("fx testFunction 0");
        boolean result = node.performNode(dummyState);

        assertTrue(result);
    }

    @Test
    public void testCorrectArgCount() {
        FunctionNode node = new FunctionNode();
        assertEquals(2, node.getArgCount());
    }
}