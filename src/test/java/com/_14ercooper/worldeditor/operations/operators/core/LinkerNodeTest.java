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
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.logical.FalseNode;
import com._14ercooper.worldeditor.operations.operators.logical.TrueNode;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import com._14ercooper.worldeditor.operations.ParserState;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LinkerNodeTest extends NodeTestCase {
    @Test
    public void testParseLinkerNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("/", "true", "false"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof LinkerNode);
        assertTrue(((LinkerNode) node).arg1 instanceof  TrueNode);
        assertTrue(((LinkerNode) node).arg2 instanceof FalseNode);
    }

    @Test
    public void testPerformLinkerNodeFirstTrue() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("/", "true", "false"));
        LinkerNode node = (LinkerNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);
        assertFalse(result);
        assertTrue(node.performed);
        assertTrue(node.arg1.performed);
        assertTrue(node.arg2.performed);
    }

    @Test
    public void testPerformLinkerNodeSecondTrue() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("/", "false", "true"));
        LinkerNode node = (LinkerNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);
        assertFalse(result);
        assertTrue(node.performed);
        assertTrue(node.arg1.performed);
        assertTrue(node.arg2.performed);
    }

    @Test
    public void testPerformLinkerNodeBothTrue() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("/", "true", "true"));
        LinkerNode node = (LinkerNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);
        assertTrue(result);
        assertTrue(node.performed);
        assertTrue(node.arg1.performed);
        assertTrue(node.arg2.performed);
    }

    @Test
    public void testPerformLinkerNodeNeitherTrue() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("/", "false", "false"));
        LinkerNode node = (LinkerNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);
        assertFalse(result);
        assertTrue(node.performed);
        assertTrue(node.arg1.performed);
        assertTrue(node.arg2.performed);
    }

    @Test
    public void testCorrectArgCount() {
        LinkerNode node = new LinkerNode();
        assertEquals(2, node.getArgCount());
    }
}