package fourteener.worldeditor.operations.operators;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;

public class FacesExposedNode extends Node {
	
	public NumberNode arg1;
	public Node arg2, arg3;
	
	public static FacesExposedNode newNode (NumberNode count, Node ifTrue, Node ifFalse) {
		FacesExposedNode facesNode = new FacesExposedNode();
		facesNode.arg1 = count;
		facesNode.arg2 = ifTrue;
		facesNode.arg3 = ifFalse;
		return facesNode;
	}
	
	public boolean performNode () {
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Performing faces exposed node, count " + Double.toString(arg1.getValue())); // -----
		
		// Count the number of faces
		// This is sorta hard to follow; sorry
		int faceCount = 0;
		if (Operator.currentBlock.getRelative(BlockFace.NORTH).getType() == Material.AIR) {
			faceCount++;
		}
		if (Operator.currentBlock.getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
			faceCount++;
		}
		if (Operator.currentBlock.getRelative(BlockFace.EAST).getType() == Material.AIR) {
			faceCount++;
		}
		if (Operator.currentBlock.getRelative(BlockFace.WEST).getType() == Material.AIR) {
			faceCount++;
		}
		if (Operator.currentBlock.getRelative(BlockFace.UP).getType() == Material.AIR) {
			faceCount++;
		}
		if (Operator.currentBlock.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
			faceCount++;
		}
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Faces exposed: " + Integer.toString(faceCount)); // -----
		
		// Perform the node
		if (faceCount >= arg1.getValue() - 0.1) {
			return arg2.performNode();
		} else {
			return arg3.performNode();
		}
	}
	
	public static int getArgCount () {
		return 3;
	}
}
