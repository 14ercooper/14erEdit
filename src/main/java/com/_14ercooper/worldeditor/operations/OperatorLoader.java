package com._14ercooper.worldeditor.operations;

import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.*;
import com._14ercooper.worldeditor.operations.operators.fun.SmallRuinNode;
import com._14ercooper.worldeditor.operations.operators.function.*;
import com._14ercooper.worldeditor.operations.operators.logical.*;
import com._14ercooper.worldeditor.operations.operators.loop.WhileNode;
import com._14ercooper.worldeditor.operations.operators.query.*;
import com._14ercooper.worldeditor.operations.operators.world.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class OperatorLoader {
    public static void LoadOperators() {
        LoadCore();
        LoadFunction();
        LoadLogical();
        LoadLoop();
        LoadQuery();
        LoadWorld();
        LoadFun();
    }

    public static List<String> nodeNames = new ArrayList<>();
    public static List<String> blockNodeNames = new ArrayList<>();
    public static List<String> numberNodeNames = new ArrayList<>();
    public static List<String> rangeNodeNames = new ArrayList<>();
    public static List<String> nextRange = new ArrayList<>();
    public static List<String> nextBlock = new ArrayList<>();

    private static void loadNode(String name, Node node) {
        nodeNames.add(name);
        if (node instanceof BlockNode) {
            blockNodeNames.add(name);
        }
        if (node instanceof NumberNode) {
            numberNodeNames.add(name);
        }
        if (node instanceof RangeNode) {
            rangeNodeNames.add(name);
        }
        if (node.isNextNodeRange()) {
            nextRange.add(name);
        }
        if (node.isNextNodeBlock()) {
            nextBlock.add(name);
        }
        Parser.addOperator(name, node);
    }

    private static void LoadCore() {
        loadNode("$$", new CraftscriptNode());
        loadNode("craftscript", Parser.getOperator(Bukkit.getConsoleSender(), "$$"));
        loadNode("script", Parser.getOperator(Bukkit.getConsoleSender(), "$$"));
        loadNode("/", new LinkerNode());
        loadNode("link", Parser.getOperator(Bukkit.getConsoleSender(), "/"));
        loadNode("$", new MacroNode());
        loadNode("macro", Parser.getOperator(Bukkit.getConsoleSender(), "$"));
        loadNode("x", new XNode());
        loadNode("y", new YNode());
        loadNode("z", new ZNode());
        loadNode("br", new BrushNode());
        loadNode("brush", Parser.getOperator(Bukkit.getConsoleSender(), "br"));
    }

    private static void LoadFunction() {
        loadNode("%", new OddsNode());
        loadNode("odds", Parser.getOperator(Bukkit.getConsoleSender(), "%"));
        loadNode("chance", Parser.getOperator(Bukkit.getConsoleSender(), "%"));
        loadNode("-", new RangeNode());
        loadNode("range", Parser.getOperator(Bukkit.getConsoleSender(), "-"));
        loadNode("inrange", Parser.getOperator(Bukkit.getConsoleSender(), "-"));
        loadNode("in_range", Parser.getOperator(Bukkit.getConsoleSender(), "-"));
        loadNode(";", new RemainderNode());
        loadNode("rem", Parser.getOperator(Bukkit.getConsoleSender(), ";"));
        loadNode("remainder", Parser.getOperator(Bukkit.getConsoleSender(), ";"));
        loadNode("mod", new ModulusNode());
        loadNode("-con", new StringContainsNode());
        loadNode(";;", new EveryXNode());
        loadNode("every", Parser.getOperator(Bukkit.getConsoleSender(), ";;"));
        loadNode("step", Parser.getOperator(Bukkit.getConsoleSender(), ";;"));
        loadNode("#", new SimplexNode());
        loadNode("simplex", Parser.getOperator(Bukkit.getConsoleSender(), "#"));
        loadNode("##", new NoiseNode());
        loadNode("noise", Parser.getOperator(Bukkit.getConsoleSender(), "##"));
        loadNode("#a", new NoiseAtNode());
        loadNode("noiseat", Parser.getOperator(Bukkit.getConsoleSender(), "#a"));
        loadNode("fx", new FunctionNode());
        loadNode("function", Parser.getOperator(Bukkit.getConsoleSender(), "fx"));
        loadNode("m#", new MultiNoiseNode());
        loadNode("multinoise", Parser.getOperator(Bukkit.getConsoleSender(), "m#"));
    }

    private static void LoadLogical() {
        loadNode("&", new AndNode());
        loadNode("and", Parser.getOperator(Bukkit.getConsoleSender(), "&"));
        loadNode("both", Parser.getOperator(Bukkit.getConsoleSender(), "&"));
        loadNode("false", new FalseNode());
        loadNode("?", new IfNode());
        loadNode("if", Parser.getOperator(Bukkit.getConsoleSender(), "?"));
        loadNode(":", new ElseNode());
        loadNode("else", Parser.getOperator(Bukkit.getConsoleSender(), ":"));
        loadNode("~", new NotNode());
        loadNode("not", Parser.getOperator(Bukkit.getConsoleSender(), "~"));
        loadNode("|", new OrNode());
        loadNode("or", Parser.getOperator(Bukkit.getConsoleSender(), "|"));
        loadNode("either", Parser.getOperator(Bukkit.getConsoleSender(), "|"));
        loadNode("true", new TrueNode());
        loadNode("<", new XorNode());
        loadNode("xor", Parser.getOperator(Bukkit.getConsoleSender(), "<"));
        loadNode("one_of", Parser.getOperator(Bukkit.getConsoleSender(), "<"));
        loadNode("anyof", new AnyOfNode());
        loadNode("any_of", Parser.getOperator(Bukkit.getConsoleSender(), "anyof"));
    }

    private static void LoadLoop() {
        loadNode("loop", new WhileNode());
        loadNode("while", Parser.getOperator(Bukkit.getConsoleSender(), "loop"));
    }

    private static void LoadQuery() {
        loadNode("@", new BlocksAdjacentNode());
        loadNode("adj", Parser.getOperator(Bukkit.getConsoleSender(), "@"));
        loadNode("adjacent", Parser.getOperator(Bukkit.getConsoleSender(), "@"));
        loadNode("blocks_adjacent", Parser.getOperator(Bukkit.getConsoleSender(), "@"));
        loadNode("^", new BlocksAboveNode());
        loadNode("blocks_above", Parser.getOperator(Bukkit.getConsoleSender(), "^"));
        loadNode("above", Parser.getOperator(Bukkit.getConsoleSender(), "^"));
        loadNode("_", new BlocksBelowNode());
        loadNode("blocks_below", Parser.getOperator(Bukkit.getConsoleSender(), "_"));
        loadNode("below", Parser.getOperator(Bukkit.getConsoleSender(), "_"));
        loadNode("*", new FacesExposedNode());
        loadNode("exposed", Parser.getOperator(Bukkit.getConsoleSender(), "*"));
        loadNode("faces_exposed", Parser.getOperator(Bukkit.getConsoleSender(), "*"));
        loadNode("<<n", new GetNBTNode());
        loadNode("get_nbt", Parser.getOperator(Bukkit.getConsoleSender(), "<<n"));
        loadNode("at", new BlockAtNode());
        loadNode("block_at", Parser.getOperator(Bukkit.getConsoleSender(), "at"));
        loadNode("sky", new SkylightNode());
        loadNode("skylight", Parser.getOperator(Bukkit.getConsoleSender(), "sky"));
        loadNode("bl", new BlocklightNode());
        loadNode("blocklight", Parser.getOperator(Bukkit.getConsoleSender(), "bl"));
        loadNode("ang", new AngleNode());
        loadNode("angle", Parser.getOperator(Bukkit.getConsoleSender(), "ang"));
        loadNode("slope", Parser.getOperator(Bukkit.getConsoleSender(), "ang"));
        loadNode("biome_is", new CheckBiomeNode());
        loadNode("get_biome", Parser.getOperator(Bukkit.getConsoleSender(), "biome_is"));
        loadNode("is_biome", Parser.getOperator(Bukkit.getConsoleSender(), "biome_is"));
        loadNode("@@", new NearbyNode());
        loadNode("near", Parser.getOperator(Bukkit.getConsoleSender(), "@@"));
        loadNode("nearby", Parser.getOperator(Bukkit.getConsoleSender(), "@@"));
        loadNode("@v", new BlocksAdjacentVerticalNode());
        loadNode("adjv", Parser.getOperator(Bukkit.getConsoleSender(), "@v"));
        loadNode("adjacent_vertical", Parser.getOperator(Bukkit.getConsoleSender(), "@v"));
        loadNode("@h", new BlocksAdjacentHorizontalNode());
        loadNode("adjh", Parser.getOperator(Bukkit.getConsoleSender(), "@h"));
        loadNode("adjacent_horizontal", Parser.getOperator(Bukkit.getConsoleSender(), "@h"));
    }

    private static void LoadWorld() {
        loadNode("<<", new GetBlockDataNode());
        loadNode("get_data", Parser.getOperator(Bukkit.getConsoleSender(), "<<"));
        loadNode("data", Parser.getOperator(Bukkit.getConsoleSender(), "<<"));
        loadNode("get_block_data", Parser.getOperator(Bukkit.getConsoleSender(), "<<"));
        loadNode("!", new IgnorePhysicsNode());
        loadNode("physics", Parser.getOperator(Bukkit.getConsoleSender(), "!"));
        loadNode("ignore_physcics", Parser.getOperator(Bukkit.getConsoleSender(), "!"));
        loadNode("same", new SameNode());
        loadNode("facing", new FacingNode());
        loadNode(">>n", new SetNBTNode());
        loadNode("set_nbt", Parser.getOperator(Bukkit.getConsoleSender(), ">>n"));
        loadNode(">", new SetNode());
        loadNode("set", Parser.getOperator(Bukkit.getConsoleSender(), ">"));
        loadNode("set_block", Parser.getOperator(Bukkit.getConsoleSender(), ">"));
        loadNode(">>", Parser.getOperator(Bukkit.getConsoleSender(), ">"));
        loadNode("grav", new GravityNode());
        loadNode("gravity", Parser.getOperator(Bukkit.getConsoleSender(), "grav"));
        loadNode("biome", new SetBiomeNode());
        loadNode("set_biome", Parser.getOperator(Bukkit.getConsoleSender(), "biome"));
        loadNode("schem", new SchemBlockNode());
        loadNode("schematic", Parser.getOperator(Bukkit.getConsoleSender(), "schem"));
        loadNode(">r", new ReplaceNode());
        loadNode("replace", Parser.getOperator(Bukkit.getConsoleSender(), ">r"));
    }

    private static void LoadFun() {
        loadNode("smallruin", new SmallRuinNode());
        loadNode("template", new TemplateNode());
        loadNode("tpl", Parser.getOperator(Bukkit.getConsoleSender(), "template"));
    }
}
