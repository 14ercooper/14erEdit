// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


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
