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