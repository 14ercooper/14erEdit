// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EveryXNodeTest extends NodeTestCase {
    @Test
    public void testParseEveryXNode() {
        Node node = parseFromString(";; x 3");
        assertTrue(node instanceof EveryXNode);
        assertEquals(0, ((EveryXNode) node).arg1);
        assertNotNull(((EveryXNode) node).arg2);

        node = parseFromString(";; y 3");
        assertTrue(node instanceof EveryXNode);
        assertEquals(1, ((EveryXNode) node).arg1);
        assertNotNull(((EveryXNode) node).arg2);

        node = parseFromString(";; z 3");
        assertTrue(node instanceof EveryXNode);
        assertEquals(2, ((EveryXNode) node).arg1);
        assertNotNull(((EveryXNode) node).arg2);
    }

    @Test
    public void testPerformEveryXNode() {
        EveryXNode node = (EveryXNode) parseFromString(";; x 2");
        boolean result = node.performNode(dummyState);
        assertTrue(result);

        node = (EveryXNode) parseFromString(";; x 3");
        result = node.performNode(dummyState);
        assertFalse(result);
    }

    @Test
    public void testCorrectArgCount() {
        EveryXNode everyXNode = new EveryXNode();
        assertEquals(2, everyXNode.getArgCount());
    }
}