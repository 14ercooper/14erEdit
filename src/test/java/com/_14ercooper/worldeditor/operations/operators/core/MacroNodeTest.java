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

import javax.crypto.Mac;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MacroNodeTest extends NodeTestCase {
    @Test
    public void testParseMacroNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("$", "test{test}"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof MacroNode);
        assertEquals("test{test}", ((MacroNode) node).arg.contents);
    }

    @Test
    public void testPerformMacroNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("$", "test{test}"));
        MacroNode node = (MacroNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);

        assertFalse(result);
    }

    @Test
    public void testPerformBadMacroNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("$", "testtest"));
        MacroNode node = (MacroNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);

        assertFalse(result);
    }

    @Test
    public void testCorrectArgCount() {
        MacroNode macroNode = new MacroNode();

        assertEquals(1, macroNode.getArgCount());
    }
}