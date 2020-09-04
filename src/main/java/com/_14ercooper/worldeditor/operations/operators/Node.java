package com._14ercooper.worldeditor.operations.operators;

public abstract class Node {

    public abstract Node newNode();

    public abstract boolean performNode();

    public abstract int getArgCount();
}
