// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


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