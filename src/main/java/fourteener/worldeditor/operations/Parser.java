package fourteener.worldeditor.operations;

import org.bukkit.Bukkit;
import org.bukkit.Material;

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
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Building entry node from root node"); // -----
		EntryNode entryNode = EntryNode.createEntryNode(rootNode);
		return entryNode;
	}
	
	// This is the massive recursive nightmare
	private static Node parsePart () {
		index++;
		
		if (parts[index].equals("~")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Not node created"); // -----
			return NotNode.newNode(parsePart());
		}
		else if (parts[index].equals("%")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Odds node created"); // -----
			return OddsNode.newNode(parseNumberNode());
		}
		else if (parts[index].equals("&")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] And node created"); // -----
			return AndNode.newNode(parsePart(), parsePart());
		}
		else if (parts[index].equals("|")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Or node created"); // -----
			return OrNode.newNode(parsePart(), parsePart());
		}
		else if (parts[index].equals("?")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] If node created"); // -----
			return IfNode.newNode(parsePart(), parsePart(), parsePart());
		}
		else if (parts[index].equals(">")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Set node created"); // -----
			return SetNode.newNode(parsePart());
		}
		else if (parts[index].equals(">>")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Set plus node created"); // -----
			return SetPlusNode.newNode(parseStringNode());
		}
		else if (parts[index].equals("<<")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Get block data node created"); // -----
			return GetBlockDataNode.newNode();
		}
		else if (parts[index].equals("false")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] False node created"); // -----
			return FalseNode.newNode();
		}
		else if (parts[index].equals("true")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] True node created"); // -----
			return TrueNode.newNode();
		}
		else if (parts[index].equals("*")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Faces exposed node created"); // -----
			return FacesExposedNode.newNode(parseNumberNode());
		}
		else if (parts[index].equals("@")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Block adjacent node created"); // -----
			return BlockAdjacentNode.newNode(parsePart(), parseNumberNode());
		}
		else if (parts[index].equals("<")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] XOR node created"); // -----
			return XorNode.newNode(parsePart(), parsePart());
		}
		else if (parts[index].equals("-")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Range node created"); // -----
			return RangeNode.newNode(parseNumberNode(), parseNumberNode());
		}
		else if (parts[index].equals("$")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Macro node created"); // -----
			return MacroNode.newNode(parseStringNode());
		}
		else if (parts[index].equals("$$")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Craftscript node created"); // -----
			return CraftscriptNode.newNode(parseStringNode());
		}
		else if (parts[index].equals("^")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Blocks above node created"); // -----
			return BlocksAboveNode.newNode(parseRangeNode(), parsePart());
		}
		else if (parts[index].equals("_")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Blocks below node created"); // -----
			return BlocksBelowNode.newNode(parseRangeNode(), parsePart());
		}
		else if (parts[index].equals("!")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Ignore physics node created"); // -----
			return IgnorePhysicsNode.newNode(parsePart());
		}
		else if (parts[index].equals("#")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Simplex noise node created"); // -----
			return SimplexNode.newNode(parseNumberNode(), parseNumberNode());
		}
		else if (parts[index].equals(";")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Remainder node created"); // -----
			return RemainderNode.newNode(parseStringNode(), parseNumberNode());
		}
		else if (parts[index].equals("same")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Same node created"); // -----
			return SameNode.newNode();
		}
		else if (Material.matchMaterial(parts[index]) != null) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Block node created, type " + Material.matchMaterial(parts[index]).name()); // -----
			return BlockNode.newNode(Material.matchMaterial(parts[index]));
		}
		else {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] New node created"); // -----
			return new Node();
		}
	}
	
	private static NumberNode parseNumberNode () {
		index ++;
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Number node created"); // -----
		return NumberNode.newNode(parts[index]);
	}
	
	private static RangeNode parseRangeNode () {
		index ++;
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Range node created"); // -----
		return RangeNode.newNode(parseNumberNode(), parseNumberNode());
	}
	
	private static String parseStringNode () {
		index ++;
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] String node created"); // -----
		return parts[index];
	}
}
