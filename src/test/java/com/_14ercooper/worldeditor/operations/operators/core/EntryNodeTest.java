/**
 * This file is part of 14erEdit.
 *
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import com._14ercooper.worldeditor.operations.operators.logical.FalseNode;
import com._14ercooper.worldeditor.operations.operators.logical.TrueNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EntryNodeTest extends NodeTestCase {
    @Test
    public void testNullNode() {
        EntryNode node = new EntryNode(null, 1);

        assertNull(node.node);
    }

    @Test
    public void testValidNode() {
        StringNode stringNode = new StringNode();
        stringNode.contents = "test";
        EntryNode node = new EntryNode(stringNode, 1);

        assertTrue(node.node instanceof StringNode);
        assertEquals("test", ((StringNode) node.node).contents);
    }

    @Test
    public void testPerformNode() {
        EntryNode falseNode = new EntryNode(new FalseNode(), 1);
        EntryNode trueNode = new EntryNode(new TrueNode(), 1);

        assertFalse(falseNode.performNode(dummyState));
        assertTrue(trueNode.performNode(dummyState));
    }

    @Test
    public void testConsumerArgsCount() {
        EntryNode node = Parser.parseOperation(dummyCommandSender, "if true false true");
        assertEquals(3, node.consumedArgs);

        node = Parser.parseOperation(dummyCommandSender, "set stone gravel");
        assertEquals(2, node.consumedArgs);

        node = Parser.parseOperation(dummyCommandSender, "true false true");
        assertEquals(1, node.consumedArgs);
    }
}