package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.testing.NodeTestCase;
import com._14ercooper.worldeditor.operations.operators.logical.FalseNode;
import com._14ercooper.worldeditor.operations.operators.logical.TrueNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntryNodeTest extends NodeTestCase {
    @Test
    public void testNullNode() {
        EntryNode node = new EntryNode(null);

        assertNull(node.node);
    }

    @Test
    public void testValidNode() {
        StringNode stringNode = new StringNode();
        stringNode.contents = "test";
        EntryNode node = new EntryNode(stringNode);

        assertTrue(node.node instanceof StringNode);
        assertEquals("test", ((StringNode) node.node).contents);
    }

    @Test
    public void testPerformNode() {
        EntryNode falseNode = new EntryNode(new FalseNode());
        EntryNode trueNode = new EntryNode(new TrueNode());

        assertFalse(falseNode.performNode(dummyState));
        assertTrue(trueNode.performNode(dummyState));
    }
}