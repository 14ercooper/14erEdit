package com.fourteener.worldeditor.operations.operators.variable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.type.MonsterVar;

public class GetMonsterCommandNode extends Node {
	
	String name;
	String path;
	
	public GetMonsterCommandNode newNode() {
		GetMonsterCommandNode node = new GetMonsterCommandNode();
		node.name = GlobalVars.operationParser.parseStringNode().contents;
		node.path = "plugins/14erEdit/Commands/" + GlobalVars.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		if (!Operator.monsterVars.containsKey(name)) {
			Main.logError("Error performing get monster command node. Please check your syntax (does the variable exist?).", Operator.currentPlayer);
			return false;
		}
		MonsterVar var = Operator.monsterVars.get(name);
		String command = "summon minecraft:" + var.getType();
		command += " ~ ~ ~ ";
		command += var.getNBT();
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
