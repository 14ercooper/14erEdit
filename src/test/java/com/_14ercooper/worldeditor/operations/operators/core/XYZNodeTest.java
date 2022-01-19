// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


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