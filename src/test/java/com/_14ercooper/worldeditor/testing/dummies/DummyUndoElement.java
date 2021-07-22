package com._14ercooper.worldeditor.testing.dummies;

import com._14ercooper.worldeditor.undo.UndoElement;
import kotlin.coroutines.Continuation;
import org.bukkit.block.BlockState;

public class DummyUndoElement extends UndoElement {
    public DummyUndoElement() {
        super("dummy", new DummyUserUndo());
    }

    @Override
    public boolean addBlock(BlockState blockFrom, BlockState blockTo) {
        return false;
    }

    @Override
    public boolean startApplyUndo() {
        return false;
    }

    @Override
    public boolean applyUndo(long blockCount) {
        return false;
    }

    @Override
    public boolean finalizeUndo() {
        return false;
    }

    @Override
    public boolean startApplyRedo() {
        return false;
    }

    @Override
    public boolean applyRedo(long blockCount) {
        return false;
    }

    @Override
    public boolean finalizeRedo() {
        return false;
    }

    @Override
    public boolean setBlock(String world, int x, int y, int z, String type, String data, String nbt) {
        return false;
    }

    @Override
    public Object flush(Continuation<? super Boolean> suspend) {
        return false;
    }
}
