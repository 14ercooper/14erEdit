package fourteener.worldeditor.operations;

import org.bukkit.Material;

import fourteener.worldeditor.operations.operators.AndNode;
import fourteener.worldeditor.operations.operators.BlockNode;
import fourteener.worldeditor.operations.operators.EntryNode;
import fourteener.worldeditor.operations.operators.FacesHiddenNode;
import fourteener.worldeditor.operations.operators.FalseNode;
import fourteener.worldeditor.operations.operators.IfNode;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.NotNode;
import fourteener.worldeditor.operations.operators.NumberNode;
import fourteener.worldeditor.operations.operators.OddsNode;
import fourteener.worldeditor.operations.operators.OrNode;
import fourteener.worldeditor.operations.operators.SetNode;
import fourteener.worldeditor.operations.operators.TrueNode;

public class Parser {
	// This starts as -1 since the first thing the parser does is increment it
	static int index = -1;
	static String[] parts;
	
	public static EntryNode parseOperation (String op) {
		// Here there be parsing magic
		// A massive recursive nightmare
		index = -1;
		parts = op.split(" *");
		Node rootNode = parsePart();
		
		// This is an error if this is true
		if (rootNode.equals(new Node()))
			return null;
		
		// Generate the entry node of the operation
		EntryNode entryNode = EntryNode.createEntryNode(rootNode);
		return entryNode;
	}
	
	// This is the massive recursive nightmare
	private static Node parsePart () {
		index++;
		
		if (parts[index].equals("~")) {
			return NotNode.newNode(parsePart());
		} else if (parts[index].equals("%")) {
			return OddsNode.newNode(parseNumberNode(), parsePart(), parsePart());
		} else if (parts[index].equals("&")) {
			return AndNode.newNode(parsePart(), parsePart());
		} else if (parts[index].equals("|")) {
			return OrNode.newNode(parsePart(), parsePart());
		} else if (parts[index].equals("?")) {
			return IfNode.newNode(parsePart(), parsePart(), parsePart());
		} else if (parts[index].equals(">")) {
			return SetNode.newNode(parseBlockNode());
		} else if (parts[index].equals("false")) {
			return FalseNode.newNode();
		} else if (parts[index].equals("true")) {
			return TrueNode.newNode();
		} else if (parts[index].equals("*")) {
			return FacesHiddenNode.newNode(parseNumberNode(), parsePart(), parsePart());
		} else {
			return new Node();
		}
	}
	
	private static BlockNode parseBlockNode () {
		index++;
		return BlockNode.newNode(Material.getMaterial(parts[index - 1]));
	}
	
	private static NumberNode parseNumberNode () {
		index ++;
		return NumberNode.newNode(parts[index - 1]);
	}
}
