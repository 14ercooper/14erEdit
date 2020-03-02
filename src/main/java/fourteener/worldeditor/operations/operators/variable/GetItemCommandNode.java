package fourteener.worldeditor.operations.operators.variable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.ItemVar;

public class GetItemCommandNode extends Node {
	
	String name;
	String path;
	
	public static GetItemCommandNode newNode (String val1, String val2) {
		GetItemCommandNode node =  new GetItemCommandNode();
		node.name = val1;
		node.path = "plugins/14erEdit/Commands/" + val2;
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
	
	public static int getArgCount () {
		return 2;
	}
}
