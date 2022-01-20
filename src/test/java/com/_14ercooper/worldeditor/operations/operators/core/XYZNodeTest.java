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

import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class XYZNodeTest extends NodeTestCase {
    @Test
    public void testParseXNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("x"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof XNode);
    }

    @Test
    public void testParseYNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("y"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof YNode);
    }

    @Test
    public void testParseZNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("z"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof ZNode);
    }

    @Test
    public void testPerformXNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("x"));
        XNode node = (XNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);
        assertFalse(result);
        assertTrue(node.performed);
        assertEquals(14, node.getValue(dummyState));
    }

    @Test
    public void testPerformYNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("y"));
        YNode node = (YNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);
        assertFalse(result);
        assertTrue(node.performed);
        assertEquals(1414, node.getValue(dummyState));
    }

    @Test
    public void testPerformZNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("z"));
        ZNode node = (ZNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);
        assertFalse(result);
        assertTrue(node.performed);
        assertEquals(141414, node.getValue(dummyState));
    }

    @Test
    public void testCorrectArgCount() {
        XNode xNode = new XNode();
        YNode yNode = new YNode();
        ZNode zNode = new ZNode();

        assertEquals(0, xNode.getArgCount());
        assertEquals(0, yNode.getArgCount());
        assertEquals(0, zNode.getArgCount());
    }
}