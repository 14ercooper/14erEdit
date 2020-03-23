package com.fourteener.worldeditor.operations.operators.variable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.type.ItemVar;

public class GetItemCommandNode extends Node {
	
	String name;
	String path;
	
	public GetItemCommandNode newNode() {
		GetItemCommandNode node = new GetItemCommandNode();
		node.name = GlobalVars.operationParser.parseStringNode();
		node.path = "plugins/14erEdit/Commands/" + GlobalVars.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		ItemVar iv = Operator.itemVars.get(name);
		String command = "give @s ";
		command += iv.getType().toLowerCase();
		command += iv.getNBT() + " ";
		command += iv.getCount();
		Main.logDebug("Command: " + command);
		try {
			File f = new File(path.replace('/', File.separatorChar));
			if (!f.exists()) {
				f.getParentFile().mkdir();
				f.getParentFile().getParentFile().mkdir();
				f.createNewFile();
			}
			else {
				f.delete();
				f.getParentFile().mkdir();
				f.getParentFile().getParentFile().mkdir();
				f.createNewFile();
			}
			Files.write(Paths.get(path.replace('/', File.separatorChar)), command.getBytes());
		} catch (IOException e) {
			Main.logDebug("Could not open file");
		}
		return true;
	}
	
	public int getArgCount () {
		return 2;
	}
}
