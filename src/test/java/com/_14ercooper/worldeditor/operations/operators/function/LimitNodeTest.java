// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.XNode;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitNodeTest extends NodeTestCase {
    @Test
    public void testParseLimitNode() {
        Node node = parseFromString("limit 5 7");
        assertTrue(node instanceof LimitNode);
        assertNotNull(((LimitNode) node).arg1);
        assertNotNull(((LimitNode) node).arg2);

        node = parseFromString("limit x 7");
        assertTrue(node instanceof LimitNode);
        assertTrue(((LimitNode) node).arg1 instanceof XNode);
        assertNotNull(((LimitNode) node).arg2);
    }

    @Test
    public void testPerformLimitNode() {
        LimitNode node = (LimitNode) parseFromString("limit 3 5");
        assertTrue(node.performNode(dummyState));

        node = (LimitNode) parseFromString("limit 7 5");
        assertFalse(node.performNode(dummyState));
    }

    @Test
    public void testCorrectArgCount() {
        LimitNode limitNode = new LimitNode();
        assertEquals(2, limitNode.getArgCount());
    }
}