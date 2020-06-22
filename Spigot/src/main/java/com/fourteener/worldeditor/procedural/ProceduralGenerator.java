package com.fourteener.worldeditor.procedural;

import java.util.List;

public abstract class ProceduralGenerator {
	// This function should have an upper bound of 2 seconds runtime
	public abstract String generate (List<String> args);
}
