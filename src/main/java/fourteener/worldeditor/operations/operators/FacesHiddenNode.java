package fourteener.worldeditor.operations.operators;

import org.bukkit.Material;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;

public class FacesHiddenNode extends Node {
	
	public NumberNode arg1;
	public Node arg2, arg3;
	
	public static FacesHiddenNode newNode (NumberNode count, Node ifTrue, Node ifFalse) {
		FacesHiddenNode facesNode = new FacesHiddenNode();
		facesNode.arg1 = count;
		facesNode.arg2 = ifTrue;
		facesNode.arg3 = ifFalse;
		return facesNode;
	}
	
	public boolean performNode () {
		// Count the number of faces
		// This is sorta hard to follow; sorry
		int faceCount = 6;
		int x,y,z;
		x = Operator.currentBlock.getX();
		y = Operator.currentBlock.getY();
		z = Operator.currentBlock.getZ();
		if (Main.world.getBlockAt(--x, y, z).equals(Material.AIR))
			faceCount--;
		x++;
		if (Main.world.getBlockAt(++x, y, z).equals(Material.AIR))
			faceCount--;
		x--;
		if (Main.world.getBlockAt(x, --y, z).equals(Material.AIR))
			faceCount--;
		y++;
		if (Main.world.getBlockAt(x, ++y, z).equals(Material.AIR))
			faceCount--;
		y--;
		if (Main.world.getBlockAt(x, y, --z).equals(Material.AIR))
			faceCount--;
		z++;
		if (Main.world.getBlockAt(x, y, ++z).equals(Material.AIR))
			faceCount--;
		z--;
		
		// Perform the node
		if (faceCount >= arg1.getValue() + 0.1) {
			return arg2.performNode();
		} else {
			return arg3.performNode();
		}
	}
	
	public static int getArgCount () {
		return 2;
	}
}
