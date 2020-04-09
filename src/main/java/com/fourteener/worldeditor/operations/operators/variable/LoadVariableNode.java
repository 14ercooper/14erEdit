package com.fourteener.worldeditor.operations.operators.variable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.type.*;

public class LoadVariableNode extends Node {

	String type, name, path;
	
	@Override
	public LoadVariableNode newNode() {
		LoadVariableNode node = new LoadVariableNode();
		node.type = GlobalVars.operationParser.parseStringNode().contents;
		node.name = GlobalVars.operationParser.parseStringNode().contents;
		node.path = "plugins/14erEdit/vars/" + GlobalVars.operationParser.parseStringNode().contents.replace('/', File.pathSeparatorChar);
		return node;
	}

	@Override
	public boolean performNode() {
		File f = new File(path);
		f.getParentFile().mkdirs();
		if (type.equalsIgnoreCase("itm")) {
			try {
				ObjectInputStream oi = new ObjectInputStream(new FileInputStream(path));
				Object obj = oi.readObject();
				Operator.itemVars.put(name, (ItemVar) obj);
				oi.close();
				return true;
			} catch (IOException e) {
				Main.logDebug(e.getMessage());
			}
			catch (ClassNotFoundException e) {
				Main.logDebug(e.getMessage());
			}
		}
		if (type.equalsIgnoreCase("mob")) {
			try {
				ObjectInputStream oi = new ObjectInputStream(new FileInputStream(path));
				Object obj = oi.readObject();
				Operator.monsterVars.put(name, (MonsterVar) obj);
				oi.close();
				return true;
			} catch (IOException e) {
				Main.logDebug(e.getMessage());
			}
			catch (ClassNotFoundException e) {
				Main.logDebug(e.getMessage());
			}
		}
		if (type.equalsIgnoreCase("spwn")) {
			try {
				ObjectInputStream oi = new ObjectInputStream(new FileInputStream(path));
				Object obj = oi.readObject();
				Operator.spawnerVars.put(name, (SpawnerVar) obj);
				oi.close();
				return true;
			} catch (IOException e) {
				Main.logDebug(e.getMessage());
			}
			catch (ClassNotFoundException e) {
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
