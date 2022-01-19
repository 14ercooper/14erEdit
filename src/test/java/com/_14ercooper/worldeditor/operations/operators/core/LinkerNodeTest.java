// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


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