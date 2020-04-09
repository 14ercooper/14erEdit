package com.fourteener.worldeditor.operations.operators.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class LoadFromFileNode extends Node {
	
	StringNode path;
	
	public LoadFromFileNode newNode() {
		LoadFromFileNode node = new LoadFromFileNode();
		node.path = GlobalVars.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		if (!Operator.fileLoads.containsKey(path.contents)) {
			List<String> lines = new ArrayList<String>();
			try {
				lines = Files.readAllLines(Paths.get(("plugins/14erEdit/ops/" + path).replace("/", File.separator)));
			} catch (IOException e) {
				Main.logDebug("Issue opening file");
			}
			List<String> newOperators = new ArrayList<String>();
			for (String s : lines) {
				String[] strArr = s.split("\\s+");
				for (String str : strArr) {
					newOperators.add(str);
				}
				
			}
			String toParse = "";
			for (String s : newOperators) {
				toParse += s + " ";
			}
			Operator o = new Operator(toParse);
			Operator.fileLoads.put(path.contents, o);
		}
		Operator o = Operator.fileLoads.get(path.contents);
		return o.messyOperate();
	}
	
	public int getArgCount () {
		return 1;
	}
}
