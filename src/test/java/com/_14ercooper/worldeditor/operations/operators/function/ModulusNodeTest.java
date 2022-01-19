// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModulusNodeTest extends NodeTestCase {
    @Test
    public void testParseModulusNode() {
        Node node = parseFromString("mod x 4 0 2");

        assertTrue(node instanceof ModulusNode);
        assertNotNull(((ModulusNode) node).arg1);
        assertNotNull(((ModulusNode) node).arg2);
        assertNotNull(((ModulusNode) node).arg3);
        assertNotNull(((ModulusNode) node).arg4);
    }

    @Test
    public void testPerformModulusNode() {
        ModulusNode node = (ModulusNode) parseFromString("mod 0 4 0 2");
        assertTrue(node.performNode(dummyState));

        node = (ModulusNode) parseFromString("mod 1 4 0 2");
        assertTrue(node.performNode(dummyState));

        node = (ModulusNode) parseFromString("mod 2 4 0 2");
        assertFalse(node.performNode(dummyState));

        node = (ModulusNode) parseFromString("mod 3 4 0 2");
        assertFalse(node.performNode(dummyState));

        node = (ModulusNode) parseFromString("mod 4 4 0 2");
        assertTrue(node.performNode(dummyState));
    }

    @Test
    public void testPerformModulusNodeNegatives() {
        ModulusNode node = (ModulusNode) parseFromString("mod -1 4 0 2");
        assertFalse(node.performNode(dummyState));

        node = (ModulusNode) parseFromString("mod -2 4 0 2");
        assertFalse(node.performNode(dummyState));

        node = (ModulusNode) parseFromString("mod -3 4 0 2");
        assertTrue(node.performNode(dummyState));

        node = (ModulusNode) parseFromString("mod -4 4 0 2");
        assertTrue(node.performNode(dummyState));

        node = (ModulusNode) parseFromString("mod -5 4 0 2");
        assertFalse(node.performNode(dummyState));
    }

    @Test
    public void testCorrectArgsCount() {
        ModulusNode modulusNode = new ModulusNode();

        assertEquals(4, modulusNode.getArgCount());
    }
}