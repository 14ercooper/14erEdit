package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FunctionNodeTest extends NodeTestCase {
    @Test
    public void testParseFunctionNode() {
        Node node = parseFromString("fx testFunction 0 notAnArg");
        assertTrue(node instanceof FunctionNode);
        assertEquals("testFunction", ((FunctionNode) node).filename);
        assertEquals(0, ((FunctionNode) node).args.size());
    }
    @Test
    public void testParseFunctionNodeArgs() {
        Node node = parseFromString("fx testFunction 2 arg1 arg2 notAnArg");
        assertTrue(node instanceof FunctionNode);
        assertEquals("testFunction", ((FunctionNode) node).filename);
        assertEquals(2, ((FunctionNode) node).args.size());
    }

    @Test
    public void testPerformFunctionNode() {
        FunctionNode node = (FunctionNode) parseFromString("fx testFunction 0");
        boolean result = node.performNode(dummyState);

        assertTrue(result);
    }

    @Test
    public void testCorrectArgCount() {
        FunctionNode node = new FunctionNode();
        assertEquals(2, node.getArgCount());
    }
}