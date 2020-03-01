package fourteener.worldeditor.operations.operators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.type.SpawnerVar;

public class GetSpawnerCommandNode extends Node {
	
	String name;
	String path;
	
	public static GetSpawnerCommandNode newNode (String varName, String pth) {
		GetSpawnerCommandNode node = new GetSpawnerCommandNode();
		node.name = varName;
		node.path = "plugins/14erEdit/Commands/" + pth;
		return node;
	}
	
	public boolean performNode () {
		SpawnerVar var = Operator.spawnerVars.get(name);
		String command = "setblock " + Operator.currentBlock.getX();
		command += " " + Operator.currentBlock.getY();
		command += " " + Operator.currentBlock.getZ();
		command += " minecraft:spawner";
		command += var.getNBT();
		command += " replace";
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
	
	public static int getArgCount () {
		return 2;
	}
}
