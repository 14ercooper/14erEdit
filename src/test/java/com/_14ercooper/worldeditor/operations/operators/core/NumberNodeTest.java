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
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class NumberNodeTest extends NodeTestCase {
    @Test
    public void testParseNumberNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("2"));
        NumberNode node = Parser.parseNumberNode(parserState);
        assertEquals(2, node.arg);
    }

    @Test
    public void testParseRandRangeNumberNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("%-", "1", "3"));
        NumberNode node = Parser.parseNumberNode(parserState);
        assertEquals(0, node.arg);
        assertEquals(1, node.rangeMin.arg);
        assertEquals(3, node.rangeMax.arg);
        assertTrue(node.isRange);
    }

    @Test
    public void testParseNoiseRangeNumberNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("#-", "1", "3"));
        NumberNode node = Parser.parseNumberNode(parserState);
        assertEquals(0, node.arg);
        assertEquals(1, node.rangeMin.arg);
        assertEquals(3, node.rangeMax.arg);
        assertTrue(node.isNoise);
    }

    @Test
    public void testParseAbsoluteNumberNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("a2"));
        NumberNode node = Parser.parseNumberNode(parserState);
        assertEquals(2, node.arg);
        assertTrue(node.isAbsolute);
    }

    @Test
    public void testGetValue() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("2"));
        NumberNode node = Parser.parseNumberNode(parserState);
        assertEquals(2, node.getValue(dummyState));
    }

    @Test
    public void testGetValueCenter() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("2"));
        NumberNode node = Parser.parseNumberNode(parserState);
        assertEquals(2, node.getValue(dummyState, 14));
    }

    @Test
    public void testGetInt() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("2.8"));
        NumberNode node = Parser.parseNumberNode(parserState);
        assertEquals(2, node.getInt(dummyState));
    }

    @Test
    public void testGetIntCenter() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("2.8"));
        NumberNode node = Parser.parseNumberNode(parserState);
        assertEquals(2, node.getInt(dummyState, 14));
    }

    @Test
    public void testGetMaxInt() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("2.8"));
        NumberNode node = Parser.parseNumberNode(parserState);
        assertEquals(2, node.getMaxInt(dummyState));
    }

    @Test
    public void testCorrectArgCount() {
        NumberNode numberNode = new NumberNode();

        assertEquals(1, numberNode.getArgCount());
    }
}