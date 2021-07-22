package com._14ercooper.worldeditor.testing.dummies;

import com._14ercooper.worldeditor.undo.UndoElement;
import com._14ercooper.worldeditor.undo.UserUndo;
import org.jetbrains.annotations.NotNull;

public class DummyUserUndo extends UserUndo {
    public DummyUserUndo() {
        super("dummy");
    }

    @Override
    public boolean flush() {
        return false;
    }

    @Override
    public boolean undoChanges(int count) {
        return false;
    }

    @Override
    public boolean redoChanges(int count) {
        return false;
    }

    @Override
    public boolean finalizeUndo(UndoElement undo) {
        return false;
    }

    @Override
    public UndoElement getNewUndoElement() {
        return new DummyUndoElement();
    }
}
