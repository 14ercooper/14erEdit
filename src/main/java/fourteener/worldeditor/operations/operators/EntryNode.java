package fourteener.worldeditor.operations.operators;

import org.bukkit.Bukkit;

import fourteener.worldeditor.main.Main;

public class EntryNode {
	public Node node = null;
	
	public static EntryNode createEntryNode (Node newNode) {
		EntryNode entryNode = new EntryNode();
		entryNode.node = newNode;
		return entryNode;
	}
	
	public boolean performNode () {
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Performing entry node"); // -----
		return node.performNode();
	}
}
