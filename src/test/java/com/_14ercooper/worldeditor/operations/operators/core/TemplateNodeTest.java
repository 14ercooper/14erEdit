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
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.testing.NodeTestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TemplateNodeTest extends NodeTestCase {
    @Test
    public void testParseTemplateNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("template", "filename", "1", "arg1"));
        parserState.setTestDummyState(dummyState);
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof TemplateNode);
        assertEquals("plugins/14erEdit/templates/filename", ((TemplateNode) node).filename);
        assertEquals(1, ((TemplateNode) node).args.size());
    }

    @Test
    public void testParseTemplateNodeNoArgs() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("template", "filename", "0"));
        parserState.setTestDummyState(dummyState);
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof TemplateNode);
        assertEquals("plugins/14erEdit/templates/filename", ((TemplateNode) node).filename);
        assertEquals(0, ((TemplateNode) node).args.size());
    }

    @Test
    public void testParseTemplateNodeManyArgs() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("template", "filename", "3", "arg1", "arg2", "arg3"));
        parserState.setTestDummyState(dummyState);
        Node node = Parser.parsePart(parserState);

        assertTrue(node instanceof TemplateNode);
        assertEquals("plugins/14erEdit/templates/filename", ((TemplateNode) node).filename);
        assertEquals(3, ((TemplateNode) node).args.size());
    }

    @Test
    public void testPerformTemplateNode() {
        ParserState parserState = new ParserState(dummyCommandSender, Arrays.asList("template", "filename", "1", "arg1"));
        parserState.setTestDummyState(dummyState);
        TemplateNode node = (TemplateNode) Parser.parsePart(parserState);

        boolean result = node.performNode(dummyState);

        assertFalse(result);
    }

    @Test
    public void testReadFile() {
        assertThrows(IOException.class, this::readFile);
    }

    private void readFile() throws IOException {
        TemplateNode.readFile("filename");
    }

    @Test
    public void testCorrectArgCount() {
        TemplateNode templateNode = new TemplateNode();

        assertEquals(2, templateNode.getArgCount());
    }
}