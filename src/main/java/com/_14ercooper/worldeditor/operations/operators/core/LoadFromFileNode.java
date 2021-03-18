package com._14ercooper.worldeditor.operations.operators.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class LoadFromFileNode extends Node {

    StringNode path = new StringNode();

    @Override
    public LoadFromFileNode newNode() {
	LoadFromFileNode node = new LoadFromFileNode();
	try {
	    node.path = GlobalVars.operationParser.parseStringNode();
	    return node;
	}
	catch (Exception e) {
	    Main.logError("Could not create file node, no file path was found.", Operator.currentPlayer);
	    return null;
	}
    }

    @Override
    public boolean performNode() {
	if (!Operator.fileLoads.containsKey(path.contents)) {
	    List<String> lines = new ArrayList<String>();
	    try {
		lines = Files.readAllLines(
			Paths.get(("plugins/14erEdit/ops/" + path.getText()).replace("/", File.separator)));
	    }
	    catch (IOException e) {
		Main.logDebug("Issue opening file " + Paths
			.get(("plugins/14erEdit/ops/" + path.getText()).replace("/", File.separator)).toString());
		Main.logError("Could not open file \"plugins/14erEdit/ops/" + path.contents + "\". Does it exist?",
			Operator.currentPlayer);
		e.printStackTrace();
	    }
	    List<String> newOperators = new ArrayList<String>();
	    for (String s : lines) {
		String[] strArr = s.split("[\\n\\r\\t ]+");
		for (String str : strArr) {
		    newOperators.add(str);
		}

	    }
	    String toParse = "";
	    for (String s : newOperators) {
		toParse += s + " ";
	    }
	    toParse = toParse.replaceAll("\\s+", " ");
	    Operator o = new Operator(toParse, Operator.currentPlayer);
	    Operator.fileLoads.put(path.contents, o);
	}
	Operator o = Operator.fileLoads.get(path.contents);
	return o.messyOperate();
    }

    @Override
    public int getArgCount() {
	return 1;
    }
}
