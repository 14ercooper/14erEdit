package fourteener.worldeditor.operations;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.*;
import fourteener.worldeditor.operations.operators.function.*;
import fourteener.worldeditor.operations.operators.logical.*;
import fourteener.worldeditor.operations.operators.loop.*;
import fourteener.worldeditor.operations.operators.query.*;
import fourteener.worldeditor.operations.operators.variable.*;
import fourteener.worldeditor.operations.operators.world.*;

public class Parser {
	// This starts as -1 since the first thing the parser does is increment it
	static int index = -1;
	static List<String> parts;
	
	public static EntryNode parseOperation (String op) {
		
		// =[ ]{1,}(1)\new \(\)
		
		// Here there be parsing magic
		// A massive recursive nightmare
		index = -1;
		parts = Arrays.asList(op.split(" "));
		Node rootNode = parsePart();
		
		// This is an error if this is true
		// Probably user error with an invalid operation
		if (rootNode == null)
			return null;
		
		// Generate the entry node of the operation
		Main.logDebug("Building entry node from root node"); // -----
		EntryNode entryNode = new EntryNode(rootNode);
		return entryNode;
	}
	
	// This is the massive recursive nightmare
	private static Node parsePart () {
		index++;
		
		if (parts.get(index).equals("~")) {
			Main.logDebug("Not node created"); // -----
			return new NotNode(parsePart());
		}
		else if (parts.get(index).equals("%")) {
			Main.logDebug("Odds node created"); // -----
			return new OddsNode(parseNumberNode());
		}
		else if (parts.get(index).equals("&")) {
			Main.logDebug("And node created"); // -----
			return new AndNode(parsePart(), parsePart());
		}
		else if (parts.get(index).equals("|")) {
			Main.logDebug("Or node created"); // -----
			return new OrNode(parsePart(), parsePart());
		}
		else if (parts.get(index).equals("?")) {
			Main.logDebug("If node created"); // -----
			return new IfNode(parsePart(), parsePart(), parsePart());
		}
		else if (parts.get(index).equals(">")) {
			Main.logDebug("Set node created"); // -----
			return new SetNode(parsePart());
		}
		else if (parts.get(index).equals(">>")) {
			Main.logDebug("Set plus node created"); // -----
			return new SetPlusNode(parseStringNode());
		}
		else if (parts.get(index).equals("<<")) {
			Main.logDebug("Get block data node created"); // -----
			return new GetBlockDataNode();
		}
		else if (parts.get(index).equals("false")) {
			Main.logDebug("False node created"); // -----
			return new FalseNode();
		}
		else if (parts.get(index).equals("true")) {
			Main.logDebug("True node created"); // -----
			return new TrueNode();
		}
		else if (parts.get(index).equals("*")) {
			Main.logDebug("Faces exposed node created"); // -----
			return new FacesExposedNode(parseNumberNode());
		}
		else if (parts.get(index).equals("@")) {
			Main.logDebug("Block adjacent node created"); // -----
			return new BlockAdjacentNode(parsePart(), parseNumberNode());
		}
		else if (parts.get(index).equals("<")) {
			Main.logDebug("XOR node created"); // -----
			return new XorNode(parsePart(), parsePart());
		}
		else if (parts.get(index).equals("-")) {
			Main.logDebug("Range node created"); // -----
			return new RangeNode(parseNumberNode(), parseNumberNode());
		}
		else if (parts.get(index).equals("$")) {
			Main.logDebug("Macro node created"); // -----
			return new MacroNode(parseStringNode());
		}
		else if (parts.get(index).equals("$$")) {
			Main.logDebug("Craftscript node created"); // -----
			return new CraftscriptNode(parseStringNode());
		}
		else if (parts.get(index).equals("^")) {
			Main.logDebug("Blocks above node created"); // -----
			return new BlocksAboveNode(parseRangeNode(), parsePart());
		}
		else if (parts.get(index).equals("_")) {
			Main.logDebug("Blocks below node created"); // -----
			return new BlocksBelowNode(parseRangeNode(), parsePart());
		}
		else if (parts.get(index).equals("!")) {
			Main.logDebug("Ignore physics node created"); // -----
			return new IgnorePhysicsNode(parsePart());
		}
		else if (parts.get(index).equals("#")) {
			Main.logDebug("Simplex noise node created"); // -----
			return new SimplexNode(parseNumberNode(), parseNumberNode());
		}
		else if (parts.get(index).equals(";")) {
			Main.logDebug("Remainder node created"); // -----
			return new RemainderNode(parseStringNode(), parseNumberNode());
		}
		else if (parts.get(index).equals("same")) {
			Main.logDebug("Same node created"); // -----
			return new SameNode();
		}
		else if (parts.get(index).equals("num")) {
			Main.logDebug("Numeric var node created"); // -----
			return new NumericVarNode(parseStringNode());
		}
		else if (parts.get(index).equals("blk")) {
			Main.logDebug("Block var node created"); // -----
			return new BlockVarNode(parseStringNode());
		}
		else if (parts.get(index).equals("itm")) {
			Main.logDebug("Item var node created"); // -----
			return new ItemVarNode(parseStringNode());
		}
		else if (parts.get(index).equals("mob")) {
			Main.logDebug("Monster var node created"); // -----
			return new MobVarNode(parseStringNode());
		}
		else if (parts.get(index).equals(">m")) {
			Main.logDebug("Monster spawn from var node created"); // -----
			return new SpawnMonsterNode(parseStringNode());
		}
		else if (parts.get(index).equals("spwn")) {
			Main.logDebug("Spawner var node created"); // -----
			return new SpawnerVarNode(parseStringNode());
		}
		else if (parts.get(index).equals(">s")) {
			Main.logDebug("Set spawner node created"); // -----
			return new SetSpawnerNode(parseStringNode());
		}
		else if (parts.get(index).equals(">>i")) {
			Main.logDebug("Get item command node created"); // -----
			return new GetItemCommandNode(parseStringNode(), parseStringNode());
		}
		else if (parts.get(index).equals(">>m")) {
			Main.logDebug("Get monster command node created"); // -----
			return new GetMonsterCommandNode(parseStringNode(), parseStringNode());
		}
		else if (parts.get(index).equals(">>s")) {
			Main.logDebug("Get spawner command node created"); // -----
			return new GetSpawnerCommandNode(parseStringNode(), parseStringNode());
		}
		else if (parts.get(index).equals("dealloc")) {
			Main.logDebug("Deallocate variable node created"); // -----
			return new DeallocNode(parseStringNode(), parseStringNode());
		}
		else if (parts.get(index).equals("/")) {
			Main.logDebug("Linker node created"); // -----
			return new LinkerNode(parsePart(), parsePart());
		}
		else if (parts.get(index).equals(">>n")) {
			Main.logDebug("Set NBT node created"); // -----
			return new SetNBTNode(parseStringNode());
		}
		else if (parts.get(index).equals("loop")) {
			Main.logDebug("While loop node created"); // -----
			return new WhileNode(parsePart(), parsePart());
		}
		else if (parts.get(index).equals("-eq")) {
			Main.logDebug("Numeric equality node created"); // -----
			return new NumericEqualityNode(parseStringNode(), parseNumberNode());
		}
		else if (parts.get(index).equals("-lt")) {
			Main.logDebug("Numeric less than node created"); // -----
			return new NumericLessNode(parseStringNode(), parseNumberNode());
		}
		else if (parts.get(index).equals("-gt")) {
			Main.logDebug("Numeric greater than node created"); // -----
			return new NumericGreaterNode(parseStringNode(), parseNumberNode());
		}
		else if (parts.get(index).equals("-lte")) {
			Main.logDebug("Numeric less than or equal node created"); // -----
			return new NumericLessEqualNode(parseStringNode(), parseNumberNode());
		}
		else if (parts.get(index).equals("-gte")) {
			Main.logDebug("Numeric greater than or equal node created"); // -----
			return new NumericGreaterEqualNode(parseStringNode(), parseNumberNode());
		}
		else if (parts.get(index).equals(">b")) {
			Main.logDebug("Set block from variable node created"); // -----
			return new SetBlockVarNode(parseStringNode());
		}
		else if (parts.get(index).equals(">i")) {
			Main.logDebug("Get item from variable node created"); // -----
			return new GetItemVarNode(parseStringNode());
		}
		else if (parts.get(index).equals(">f")) {
			Main.logDebug("Load op from file node created"); // -----
			return new LoadFromFileNode(parseStringNode());
		}
		else if (parts.get(index).equals("var")) {
			Main.logDebug("Modify variable node created"); // -----
			return new ModifyVarNode(parseStringNode(), parseStringNode(), parseStringNode());
		}
		else if (Material.matchMaterial(parts.get(index)) != null) {
			Main.logDebug("Block node created, type " + Material.matchMaterial(parts.get(index)).name()); // -----
			return new BlockNode(Material.matchMaterial(parts.get(index)));
		}
		else if (Material.matchMaterial(parts.get(index).split("\\[")[0]) != null && parts.get(index).split("\\[").length > 1) {
			Main.logDebug("Block node created, type " + Material.matchMaterial(parts.get(index).split("\\[")[0]).name()); // -----
			BlockData bd = Bukkit.getServer().createBlockData(parts.get(index));
			Material mat = Material.matchMaterial(parts.get(index).split("\\[")[0]);
			return new BlockNode(mat, bd);
		}
		else {
			Main.logDebug("Null node created"); // -----
			return null;
		}
	}
	
	private static NumberNode parseNumberNode () {
		index ++;
		Main.logDebug("Number node created"); // -----
		return new NumberNode(parts.get(index));
	}
	
	private static RangeNode parseRangeNode () {
		index ++;
		Main.logDebug("Range node created"); // -----
		return new RangeNode(parseNumberNode(), parseNumberNode());
	}
	
	private static String parseStringNode () {
		index ++;
		Main.logDebug("String node created"); // -----
		return parts.get(index);
	}
}
