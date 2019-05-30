package fourteener.worldeditor.operations.operators;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import fourteener.worldeditor.operations.Operator;

public class FacesExposedNode extends Node {
	
	public NumberNode arg;
	
	public static FacesExposedNode newNode (NumberNode count) {
		FacesExposedNode facesNode = new FacesExposedNode();
		facesNode.arg = count;
		return facesNode;
	}
	
	public boolean performNode () {
		
		// Count the number of faces
		// Basically check for air in each of the four directions
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
		
		// Perform the node
		return (faceCount >= arg.getValue() - 0.1);
	}
	
	public static int getArgCount () {
		return 1;
	}
}
