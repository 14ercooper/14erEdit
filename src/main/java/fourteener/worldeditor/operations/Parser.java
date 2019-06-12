package fourteener.worldeditor.operations;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.operators.*;

public class Parser {
	// This starts as -1 since the first thing the parser does is increment it
	static int index = -1;
	static String[] parts;
	
	public static EntryNode parseOperation (String op) {
		
		// Here there be parsing magic
		// A massive recursive nightmare
		index = -1;
		parts = op.split(" ");
		Node rootNode = parsePart();
		
		// This is an error if this is true
		if (rootNode.equals(new Node()))
			return null;
		
		// Generate the entry node of the operation
		Main.logDebug("Building entry node from root node"); // -----
		EntryNode entryNode = EntryNode.createEntryNode(rootNode);
		return entryNode;
	}
	
	// This is the massive recursive nightmare
	private static Node parsePart () {
		index++;
		
		if (parts[index].equals("~")) {
			Main.logDebug("Not node created"); // -----
			return NotNode.newNode(parsePart());
		}
		else if (parts[index].equals("%")) {
			Main.logDebug("Odds node created"); // -----
			return OddsNode.newNode(parseNumberNode());
		}
		else if (parts[index].equals("&")) {
			Main.logDebug("And node created"); // -----
			return AndNode.newNode(parsePart(), parsePart());
		}
		else if (parts[index].equals("|")) {
			Main.logDebug("Or node created"); // -----
			return OrNode.newNode(parsePart(), parsePart());
		}
		else if (parts[index].equals("?")) {
			Main.logDebug("If node created"); // -----
			return IfNode.newNode(parsePart(), parsePart(), parsePart());
		}
		else if (parts[index].equals(">")) {
			Main.logDebug("Set node created"); // -----
			return SetNode.newNode(parsePart());
		}
		else if (parts[index].equals(">>")) {
			Main.logDebug("Set plus node created"); // -----
			return SetPlusNode.newNode(parseStringNode());
		}
		else if (parts[index].equals("<<")) {
			Main.logDebug("Get block data node created"); // -----
			return GetBlockDataNode.newNode();
		}
		else if (parts[index].equals("false")) {
			Main.logDebug("False node created"); // -----
			return FalseNode.newNode();
		}
		else if (parts[index].equals("true")) {
			Main.logDebug("True node created"); // -----
			return TrueNode.newNode();
		}
		else if (parts[index].equals("*")) {
			Main.logDebug("Faces exposed node created"); // -----
			return FacesExposedNode.newNode(parseNumberNode());
		}
		else if (parts[index].equals("@")) {
			Main.logDebug("Block adjacent node created"); // -----
			return BlockAdjacentNode.newNode(parsePart(), parseNumberNode());
		}
		else if (parts[index].equals("<")) {
			Main.logDebug("XOR node created"); // -----
			return XorNode.newNode(parsePart(), parsePart());
		}
		else if (parts[index].equals("-")) {
			Main.logDebug("Range node created"); // -----
			return RangeNode.newNode(parseNumberNode(), parseNumberNode());
		}
		else if (parts[index].equals("$")) {
			Main.logDebug("Macro node created"); // -----
			return MacroNode.newNode(parseStringNode());
		}
		else if (parts[index].equals("$$")) {
			Main.logDebug("Craftscript node created"); // -----
			return CraftscriptNode.newNode(parseStringNode());
		}
		else if (parts[index].equals("^")) {
			Main.logDebug("Blocks above node created"); // -----
			return BlocksAboveNode.newNode(parseRangeNode(), parsePart());
		}
		else if (parts[index].equals("_")) {
			Main.logDebug("Blocks below node created"); // -----
			return BlocksBelowNode.newNode(parseRangeNode(), parsePart());
		}
		else if (parts[index].equals("!")) {
			Main.logDebug("Ignore physics node created"); // -----
			return IgnorePhysicsNode.newNode(parsePart());
		}
		else if (parts[index].equals("#")) {
			Main.logDebug("Simplex noise node created"); // -----
			return SimplexNode.newNode(parseNumberNode(), parseNumberNode());
		}
		else if (parts[index].equals(";")) {
			Main.logDebug("Remainder node created"); // -----
			return RemainderNode.newNode(parseStringNode(), parseNumberNode());
		}
		else if (parts[index].equals("same")) {
			Main.logDebug("Same node created"); // -----
			return SameNode.newNode();
		}
		else if (Material.matchMaterial(parts[index]) != null) {
			Main.logDebug("Block node created, type " + Material.matchMaterial(parts[index]).name()); // -----
			return BlockNode.newNode(Material.matchMaterial(parts[index]));
		}
		else if (Material.matchMaterial(parts[index].split("\\[")[0]) != null && parts[index].split("\\[").length > 1) {
			Main.logDebug("Block node created, type " + Material.matchMaterial(parts[index].split("\\[")[0]).name()); // -----
			BlockData bd = Bukkit.getServer().createBlockData(parts[index]);
			Material mat = Material.matchMaterial(parts[index].split("\\[")[0]);
			return BlockNode.newNode(mat, bd);
		}
		else {
			Main.logDebug("New node created"); // -----
			return new Node();
		}
	}
	
	private static NumberNode parseNumberNode () {
		index ++;
		Main.logDebug("Number node created"); // -----
		return NumberNode.newNode(parts[index]);
	}
	
	private static RangeNode parseRangeNode () {
		index ++;
		Main.logDebug("Range node created"); // -----
		return RangeNode.newNode(parseNumberNode(), parseNumberNode());
	}
	
	private static String parseStringNode () {
		index ++;
		Main.logDebug("String node created"); // -----
		return parts[index];
	}
}
