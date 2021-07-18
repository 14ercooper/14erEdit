package com._14ercooper.worldeditor.operations;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.*;
import com._14ercooper.worldeditor.operations.operators.fun.SmallRuinNode;
import com._14ercooper.worldeditor.operations.operators.function.*;
import com._14ercooper.worldeditor.operations.operators.logical.*;
import com._14ercooper.worldeditor.operations.operators.loop.*;
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
        GlobalVars.operationParser.addOperator(name, node);
    }

    private static void LoadCore() {
        loadNode("$$", new CraftscriptNode());
        loadNode("craftscript", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "$$"));
        loadNode("script", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "$$"));
        loadNode("/", new LinkerNode());
        loadNode("link", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "/"));
        loadNode("$", new MacroNode());
        loadNode("macro", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "$"));
        loadNode("x", new XNode());
        loadNode("y", new YNode());
        loadNode("z", new ZNode());
        loadNode("br", new BrushNode());
        loadNode("brush", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "br"));
    }

    private static void LoadFunction() {
        loadNode("%", new OddsNode());
        loadNode("odds", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "%"));
        loadNode("chance", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "%"));
        loadNode("-", new RangeNode());
        loadNode("range", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "-"));
        loadNode("inrange", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "-"));
        loadNode("in_range", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "-"));
        loadNode(";", new RemainderNode());
        loadNode("rem", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), ";"));
        loadNode("remainder", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), ";"));
        loadNode("-con", new StringContainsNode());
        loadNode(";;", new EveryXNode());
        loadNode("every", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), ";;"));
        loadNode("step", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), ";;"));
        loadNode("#", new SimplexNode());
        loadNode("simplex", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "#"));
        loadNode("##", new NoiseNode());
        loadNode("noise", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "##"));
        loadNode("#a", new NoiseAtNode());
        loadNode("noiseat", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "#a"));
        loadNode("fx", new FunctionNode());
        loadNode("function", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "fx"));
        loadNode("m#", new MultiNoiseNode());
        loadNode("multinoise", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "m#"));
    }

    private static void LoadLogical() {
        loadNode("&", new AndNode());
        loadNode("and", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "&"));
        loadNode("both", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "&"));
        loadNode("false", new FalseNode());
        loadNode("?", new IfNode());
        loadNode("if", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "?"));
        loadNode(":", new ElseNode());
        loadNode("else", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), ":"));
        loadNode("~", new NotNode());
        loadNode("not", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "~"));
        loadNode("|", new OrNode());
        loadNode("or", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "|"));
        loadNode("either", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "|"));
        loadNode("true", new TrueNode());
        loadNode("<", new XorNode());
        loadNode("xor", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "<"));
        loadNode("one_of", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "<"));
        loadNode("anyof", new AnyOfNode());
        loadNode("any_of", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "anyof"));
    }

    private static void LoadLoop() {
        loadNode("loop", new WhileNode());
        loadNode("while", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "loop"));
    }

    private static void LoadQuery() {
        loadNode("@", new BlocksAdjacentNode());
        loadNode("adj", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "@"));
        loadNode("adjacent", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "@"));
        loadNode("blocks_adjacent", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "@"));
        loadNode("^", new BlocksAboveNode());
        loadNode("blocks_above", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "^"));
        loadNode("above", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "^"));
        loadNode("_", new BlocksBelowNode());
        loadNode("blocks_below", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "_"));
        loadNode("below", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "_"));
        loadNode("*", new FacesExposedNode());
        loadNode("exposed", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "*"));
        loadNode("faces_exposed", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "*"));
        loadNode("<<n", new GetNBTNode());
        loadNode("get_nbt", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "<<n"));
        loadNode("at", new BlockAtNode());
        loadNode("block_at", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "at"));
        loadNode("sky", new SkylightNode());
        loadNode("skylight", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "sky"));
        loadNode("bl", new BlocklightNode());
        loadNode("blocklight", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "bl"));
        loadNode("ang", new AngleNode());
        loadNode("angle", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "ang"));
        loadNode("slope", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "ang"));
        loadNode("biome_is", new CheckBiomeNode());
        loadNode("get_biome", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "biome_is"));
        loadNode("is_biome", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "biome_is"));
        loadNode("@@", new NearbyNode());
        loadNode("near", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "@@"));
        loadNode("nearby", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "@@"));
        loadNode("@v", new BlocksAdjacentVerticalNode());
        loadNode("adjv", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "@v"));
        loadNode("adjacent_vertical", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "@v"));
        loadNode("@h", new BlocksAdjacentHorizontalNode());
        loadNode("adjh", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "@h"));
        loadNode("adjacent_horizontal", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "@h"));
    }

    private static void LoadWorld() {
        loadNode("<<", new GetBlockDataNode());
        loadNode("get_data", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "<<"));
        loadNode("data", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "<<"));
        loadNode("get_block_data", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "<<"));
        loadNode("!", new IgnorePhysicsNode());
        loadNode("physics", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "!"));
        loadNode("ignore_physcics", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "!"));
        loadNode("same", new SameNode());
        loadNode("facing", new FacingNode());
        loadNode(">>n", new SetNBTNode());
        loadNode("set_nbt", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), ">>n"));
        loadNode(">", new SetNode());
        loadNode("set", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), ">"));
        loadNode("set_block", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), ">"));
        loadNode(">>", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), ">"));
        loadNode("grav", new GravityNode());
        loadNode("gravity", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "grav"));
        loadNode("biome", new SetBiomeNode());
        loadNode("set_biome", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "biome"));
        loadNode("schem", new SchemBlockNode());
        loadNode("schematic", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "schem"));
        loadNode(">r", new ReplaceNode());
        loadNode("replace", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), ">r"));
    }

    private static void LoadFun() {
        loadNode("smallruin", new SmallRuinNode());
        loadNode("template", new TemplateNode());
        loadNode("tpl", GlobalVars.operationParser.getOperator(Bukkit.getConsoleSender(), "template"));
    }
}
