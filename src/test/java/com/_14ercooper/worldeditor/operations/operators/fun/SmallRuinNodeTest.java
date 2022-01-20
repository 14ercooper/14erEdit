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

package com._14ercooper.worldeditor.operations.operators.fun;

import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.world.BlockNode;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SmallRuinNodeTest extends NodeTestCase {
    @Test
    public void testParseSmallRuinNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("smallruin", "-", "1", "3", "-", "2", "5", "-", "1", "2", "stone"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof SmallRuinNode);
        assertNotNull(((SmallRuinNode) node).xMax);
        assertNotNull(((SmallRuinNode) node).zMax);
        assertNotNull(((SmallRuinNode) node).stackSize);
        assertTrue(((SmallRuinNode) node).block instanceof BlockNode);
    }

    @Test
    public void testPerformSmallRuinNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("smallruin", "-", "1", "3", "-", "2", "5", "-", "1", "2", "stone"));
        SmallRuinNode node = (SmallRuinNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);
        assertTrue(result);
    }

    @Test
    public void testPerformSmallRuinNodeSameValues() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("smallruin", "-", "3", "3", "-", "3", "3", "-", "1", "1", "stone"));
        SmallRuinNode node = (SmallRuinNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);
        assertTrue(result);
    }

    @Test
    public void testCorrectArgCount() {
        SmallRuinNode smallRuinNode = new SmallRuinNode();
        assertEquals(3, smallRuinNode.getArgCount());
    }
}