package com._14ercooper.worldeditor.make;

import com._14ercooper.worldeditor.make.generators.*;

public class MakeSetup {
    public static void registerMakes() {
	Make.generators.put("item", new Item());
    }
}
