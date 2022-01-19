// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.brush.shapes.Voxel;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.logical.TrueNode;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BrushNodeTest extends NodeTestCase {
    @Test
    public void testParseBrushNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("br", "v", "true"));
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof BrushNode);
        assertTrue(((BrushNode) node).shape instanceof Voxel);
        assertTrue(((BrushNode) node).entry.get(0).node instanceof TrueNode);
    }

    @Test
    public void testPerformBrushNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("br", "v", "true"));
        BrushNode node = (BrushNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);

        assertTrue(result);
    }

    @Test
    public void testCorrectArgCount() {
        BrushNode brushNode = new BrushNode();

        assertEquals(3, brushNode.getArgCount());
    }
}