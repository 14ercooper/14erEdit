package com.fourteener.worldeditor.operations.operators.variable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.type.*;

public class SaveVariableNode extends Node {

	String type, name, path;
	
	@Override
	public SaveVariableNode newNode() {
		SaveVariableNode node = new SaveVariableNode();
		node.type = GlobalVars.operationParser.parseStringNode();
		node.name = GlobalVars.operationParser.parseStringNode();
		node.path = "plugins/14erEdit/vars/" + GlobalVars.operationParser.parseStringNode().replace('/', File.pathSeparatorChar);
		return node;
	}

	@Override
	public boolean performNode() {
		File f = new File(path);
		f.getParentFile().mkdirs();
		if (type.equalsIgnoreCase("itm")) {
			ItemVar var = Operator.itemVars.get(name);
			try {
				Files.deleteIfExists(f.toPath());
				ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(path));
				oo.writeObject(var);
				oo.close();
				return true;
			} catch (IOException e) {
				Main.logDebug(e.getMessage());
			}
		}
		if (type.equalsIgnoreCase("mob")) {
			MonsterVar var = Operator.monsterVars.get(name);
			try {
				Files.deleteIfExists(f.toPath());
				ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(path));
				oo.writeObject(var);
				oo.close();
				return true;
			} catch (IOException e) {
				Main.logDebug(e.getMessage());
			}
		}
		if (type.equalsIgnoreCase("spwn")) {
			SpawnerVar var = Operator.spawnerVars.get(name);
			try {
				Files.deleteIfExists(f.toPath());
				ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(path));
				oo.writeObject(var);
				oo.close();
				return true;
			} catch (IOException e) {
				Main.logDebug(e.getMessage());
			}
		}
		return false;
	}

	@Override
	public int getArgCount() {
		return 3;
	}

}
