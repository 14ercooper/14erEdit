package fourteener.worldeditor.operations.operators.query;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

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
		Block b = Main.world.getBlockAt(Operator.currentBlock.getLocation());
		if (b.getRelative(BlockFace.NORTH).getType() == Material.AIR) {
			faceCount++;
		}
		if (b.getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
			faceCount++;
		}
		if (b.getRelative(BlockFace.EAST).getType() == Material.AIR) {
			faceCount++;
		}
		if (b.getRelative(BlockFace.WEST).getType() == Material.AIR) {
			faceCount++;
		}
		if (b.getRelative(BlockFace.UP).getType() == Material.AIR) {
			faceCount++;
		}
		if (b.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
			faceCount++;
		}
		
		// Perform the node
		return (faceCount >= arg.getValue() - 0.1);
	}
	
	public static int getArgCount () {
		return 1;
	}
}
