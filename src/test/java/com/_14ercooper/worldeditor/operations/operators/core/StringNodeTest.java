package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.world.BlockNode;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class StringNodeTest extends NodeTestCase {
    @Test
    public void testParseStringNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("blah"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof BlockNode);
        assertEquals("blah", ((BlockNode) node).blockList.get(0).mat);
    }

    @Test
    public void testParseStringNode2() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("blah"));
        StringNode node = Parser.parseStringNode(parserState);

        assertEquals("blah", node.getText());
    }

    @Test
    public void testParseStringNode3() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("blah"));
        parserState.setIndex(0);
        StringNode node = (new StringNode()).newNode(parserState);

        assertEquals("undefined", node.getText());
    }

    @Test
    public void testPerformStringNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("stone"));
        Node node = Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);

        assertTrue(result);
    }

    @Test
    public void testPerformStringNodeNotMatch() {
        ParserState parserState = new ParserState(dummyCommandSender, Collections.singletonList("gravel"));
        Node node = Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);

        assertFalse(result);
    }

    @Test
    public void testCorrectArgCount() {
        StringNode stringNode = new StringNode();

        assertEquals(1, stringNode.getArgCount());
    }
}