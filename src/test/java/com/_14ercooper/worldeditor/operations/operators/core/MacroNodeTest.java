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