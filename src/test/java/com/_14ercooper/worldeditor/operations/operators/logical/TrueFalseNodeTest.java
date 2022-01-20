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

package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TrueFalseNodeTest extends NodeTestCase {
    @Test
    public void testParseTrueNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("true"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof TrueNode);
    }

    @Test
    public void testParseFalseNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("false"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof FalseNode);
    }

    @Test
    public void testPerformTrueNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("true"));
        Node node = Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);

        assertTrue(node.performed);
        assertTrue(result);
    }

    @Test
    public void testPerformFalseNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("false"));
        Node node = Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);

        assertTrue(node.performed);
        assertFalse(result);
    }

    @Test
    public void testCorrectArgCount() {
        TrueNode trueNode = new TrueNode();
        FalseNode falseNode = new FalseNode();

        assertEquals(0, trueNode.getArgCount());
        assertEquals(0, falseNode.getArgCount());
    }
}