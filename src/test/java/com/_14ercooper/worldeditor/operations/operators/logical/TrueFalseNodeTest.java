// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


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