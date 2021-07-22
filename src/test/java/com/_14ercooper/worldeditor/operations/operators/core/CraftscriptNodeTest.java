package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CraftscriptNodeTest extends NodeTestCase {
    @Test
    public void testParseCraftscriptNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("$$", "test{test}"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof CraftscriptNode);
        assertEquals("test{test}", ((CraftscriptNode) node).arg.contents);
    }

    @Test
    public void testPerformCraftscriptNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("$$", "test{test}"));
        CraftscriptNode node = (CraftscriptNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);

        assertTrue(result);
    }

    @Test
    public void testPerformBadCraftscriptNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("$$", "testtest"));
        CraftscriptNode node = (CraftscriptNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);

        assertFalse(result);
    }

    @Test
    public void testCorrectArgCount() {
        CraftscriptNode craftscriptNode = new CraftscriptNode();

        assertEquals(1, craftscriptNode.getArgCount());
    }
}