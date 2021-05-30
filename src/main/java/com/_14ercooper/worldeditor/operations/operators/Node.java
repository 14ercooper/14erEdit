package com._14ercooper.worldeditor.operations.operators;

public abstract class Node {

    public boolean isNextNodeRange() {
        return false;
    }

    public boolean isNextNodeBlock() {
        return false;
    }

    public abstract Node newNode();

    public abstract boolean performNode();

    public abstract int getArgCount();
}
