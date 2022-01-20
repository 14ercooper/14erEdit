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

package com._14ercooper.worldeditor.testing;

import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.blockiterator.IteratorLoader;
import com._14ercooper.worldeditor.brush.BrushLoader;
import com._14ercooper.worldeditor.macros.MacroLoader;
import com._14ercooper.worldeditor.operations.OperatorLoader;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.scripts.CraftscriptLoader;
import com._14ercooper.worldeditor.testing.dummies.DummyBlock;
import com._14ercooper.worldeditor.testing.dummies.DummyCommandSender;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.testing.dummies.DummyUndoElement;
import com._14ercooper.worldeditor.testing.dummies.DummyWorld;
import com._14ercooper.worldeditor.undo.UndoElement;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTestCase {
    public OperatorState dummyState;
    private static BlockWrapper dummyWrapper;
    public static CommandSender dummyCommandSender;
    private static World dummyWorld;
    private static UndoElement dummyUndoElement;
    private static Location dummyOrigin;

    public static Node parseFromString(String operation) {
        return Parser.parsePart(new ParserState(dummyCommandSender, Arrays.asList(operation.split("\\s+"))));
    }

    @BeforeAll
    public static void setupManagers() {
        IteratorLoader.LoadIterators();
        OperatorLoader.LoadOperators(new DummyCommandSender());
        CraftscriptLoader.LoadBundledCraftscripts();
        MacroLoader.LoadMacros();
        BrushLoader.LoadBrushes();
    }

    @BeforeEach
    public void setDummyVariables() {
        dummyWrapper = new BlockWrapper(new DummyBlock(), 14, 1414, 141414);
        dummyCommandSender = new DummyCommandSender();
        dummyWorld = new DummyWorld();
        dummyUndoElement = new DummyUndoElement();
        dummyOrigin = new Location(dummyWorld, 14, 14, 14);
        dummyState = new OperatorState(dummyWrapper, dummyCommandSender, dummyWorld, dummyUndoElement, dummyOrigin);
    }

    @AfterEach
    public void assertStateUnchanged() {
        assertEquals(dummyWrapper, dummyState.getCurrentBlock());
        assertEquals(dummyCommandSender, dummyState.getCurrentPlayer());
        assertEquals(dummyWorld, dummyState.getCurrentWorld());
        assertEquals(dummyUndoElement, dummyState.getCurrentUndo());
    }
}
