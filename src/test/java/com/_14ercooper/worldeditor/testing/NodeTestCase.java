package com._14ercooper.worldeditor.testing;

import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.blockiterator.IteratorLoader;
import com._14ercooper.worldeditor.brush.BrushLoader;
import com._14ercooper.worldeditor.macros.MacroLoader;
import com._14ercooper.worldeditor.operations.OperatorLoader;
import com._14ercooper.worldeditor.scripts.CraftscriptLoader;
import com._14ercooper.worldeditor.testing.dummies.DummyBlock;
import com._14ercooper.worldeditor.testing.dummies.DummyCommandSender;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.testing.dummies.DummyUndoElement;
import com._14ercooper.worldeditor.testing.dummies.DummyWorld;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class NodeTestCase {
    public OperatorState dummyState;
    public CommandSender dummyCommandSender;

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
        BlockWrapper dummyWrapper = new BlockWrapper(new DummyBlock(), 14, 1414, 141414);
        dummyState = new OperatorState(dummyWrapper, new DummyCommandSender(), new DummyWorld(), new DummyUndoElement());
        dummyCommandSender = new DummyCommandSender();
    }
}
