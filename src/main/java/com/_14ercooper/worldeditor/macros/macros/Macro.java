package com._14ercooper.worldeditor.macros.macros;

import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.Location;

// This is a blank macro, to use as a template for others, and so you can reference a generic macro
public abstract class Macro {

    // Run this macro
    public abstract boolean performMacro(String[] args, Location loc, OperatorState state);
}
