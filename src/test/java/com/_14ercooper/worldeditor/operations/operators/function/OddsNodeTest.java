package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OddsNodeTest extends NodeTestCase {
    @Test
    public void testParseOddsNode() {
        Node node = parseFromString("% 50");

        assertTrue(node instanceof OddsNode);
        assertNotNull(((OddsNode) node).arg);
        assertEquals(50.0, ((OddsNode) node).arg.getValue(dummyState));
    }

    @Test
    public void testPerformOddsNode() {
        Node node = parseFromString("% 50");

        Main.getRand().setSeed(14);

        assertFalse(node.performNode(dummyState));
        assertFalse(node.performNode(dummyState));
        assertTrue(node.performNode(dummyState));
        assertTrue(node.performNode(dummyState));
        assertTrue(node.performNode(dummyState));
    }

    @Test
    public void testPerformOddsNodeNoSeed() {
        Node node = parseFromString("% 50");
        int trueCount = 0;
        for (int i = 0; i < 1000000; i++) {
            if (node.performNode(dummyState)) {
                trueCount++;
            }
        }
        double empiricalOdds = (double) trueCount / 1000000.0;
        double distFrom50 = Math.abs(empiricalOdds - 0.5);

        assertTrue(distFrom50 < 0.01);
    }

    @Test
    public void testCorrectArgCount() {
        OddsNode oddsNode = new OddsNode();

        assertEquals(1, oddsNode.getArgCount());
    }
}