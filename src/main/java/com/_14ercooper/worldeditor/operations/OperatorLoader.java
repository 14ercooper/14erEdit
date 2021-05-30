package com._14ercooper.worldeditor.operations;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.*;
import com._14ercooper.worldeditor.operations.operators.fun.SmallRuinNode;
import com._14ercooper.worldeditor.operations.operators.function.EveryXNode;
import com._14ercooper.worldeditor.operations.operators.function.FunctionNode;
import com._14ercooper.worldeditor.operations.operators.function.MultiNoiseNode;
import com._14ercooper.worldeditor.operations.operators.function.NoiseNode;
import com._14ercooper.worldeditor.operations.operators.function.OddsNode;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;
import com._14ercooper.worldeditor.operations.operators.function.RemainderNode;
import com._14ercooper.worldeditor.operations.operators.function.SimplexNode;
import com._14ercooper.worldeditor.operations.operators.function.StringContainsNode;
import com._14ercooper.worldeditor.operations.operators.logical.AndNode;
import com._14ercooper.worldeditor.operations.operators.logical.AnyOfNode;
import com._14ercooper.worldeditor.operations.operators.logical.ElseNode;
import com._14ercooper.worldeditor.operations.operators.logical.FalseNode;
import com._14ercooper.worldeditor.operations.operators.logical.IfNode;
import com._14ercooper.worldeditor.operations.operators.logical.NotNode;
import com._14ercooper.worldeditor.operations.operators.logical.OrNode;
import com._14ercooper.worldeditor.operations.operators.logical.TrueNode;
import com._14ercooper.worldeditor.operations.operators.logical.XorNode;
import com._14ercooper.worldeditor.operations.operators.loop.NumericEqualityNode;
import com._14ercooper.worldeditor.operations.operators.loop.NumericGreaterEqualNode;
import com._14ercooper.worldeditor.operations.operators.loop.NumericGreaterNode;
import com._14ercooper.worldeditor.operations.operators.loop.NumericLessEqualNode;
import com._14ercooper.worldeditor.operations.operators.loop.NumericLessNode;
import com._14ercooper.worldeditor.operations.operators.loop.WhileNode;
import com._14ercooper.worldeditor.operations.operators.query.AngleNode;
import com._14ercooper.worldeditor.operations.operators.query.BlockAtNode;
import com._14ercooper.worldeditor.operations.operators.query.BlocklightNode;
import com._14ercooper.worldeditor.operations.operators.query.BlocksAboveNode;
import com._14ercooper.worldeditor.operations.operators.query.BlocksAdjacentHorizontalNode;
import com._14ercooper.worldeditor.operations.operators.query.BlocksAdjacentNode;
import com._14ercooper.worldeditor.operations.operators.query.BlocksAdjacentVerticalNode;
import com._14ercooper.worldeditor.operations.operators.query.BlocksBelowNode;
import com._14ercooper.worldeditor.operations.operators.query.CheckBiomeNode;
import com._14ercooper.worldeditor.operations.operators.query.FacesExposedNode;
import com._14ercooper.worldeditor.operations.operators.query.GetNBTNode;
import com._14ercooper.worldeditor.operations.operators.query.NearbyNode;
import com._14ercooper.worldeditor.operations.operators.query.NoiseAtNode;
import com._14ercooper.worldeditor.operations.operators.query.SkylightNode;
import com._14ercooper.worldeditor.operations.operators.variable.BlockVarNode;
import com._14ercooper.worldeditor.operations.operators.variable.DeallocNode;
import com._14ercooper.worldeditor.operations.operators.variable.GetItemCommandNode;
import com._14ercooper.worldeditor.operations.operators.variable.GetItemVarNode;
import com._14ercooper.worldeditor.operations.operators.variable.GetMonsterCommandNode;
import com._14ercooper.worldeditor.operations.operators.variable.GetMonsterItemNode;
import com._14ercooper.worldeditor.operations.operators.variable.GetSpawnerCommandNode;
import com._14ercooper.worldeditor.operations.operators.variable.GetSpawnerItemNode;
import com._14ercooper.worldeditor.operations.operators.variable.ItemVarNode;
import com._14ercooper.worldeditor.operations.operators.variable.LoadVariableNode;
import com._14ercooper.worldeditor.operations.operators.variable.MobVarNode;
import com._14ercooper.worldeditor.operations.operators.variable.ModifyVarNode;
import com._14ercooper.worldeditor.operations.operators.variable.NumericVarNode;
import com._14ercooper.worldeditor.operations.operators.variable.SaveVariableNode;
import com._14ercooper.worldeditor.operations.operators.variable.SetBlockVarNode;
import com._14ercooper.worldeditor.operations.operators.variable.SetSpawnerNode;
import com._14ercooper.worldeditor.operations.operators.variable.SpawnMonsterNode;
import com._14ercooper.worldeditor.operations.operators.variable.SpawnerVarNode;
import com._14ercooper.worldeditor.operations.operators.world.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperatorLoader {
    public static void LoadOperators() {
        LoadCore();
        LoadFunction();
        LoadLogical();
        LoadLoop();
        LoadQuery();
        LoadVariable();
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
        loadNode("craftscript", GlobalVars.operationParser.getOperator("$$"));
        loadNode("script", GlobalVars.operationParser.getOperator("$$"));
        loadNode("/", new LinkerNode());
        loadNode("link", GlobalVars.operationParser.getOperator("/"));
        loadNode(">f", new LoadFromFileNode());
        loadNode("file", GlobalVars.operationParser.getOperator(">f"));
        loadNode("$", new MacroNode());
        loadNode("macro", GlobalVars.operationParser.getOperator("$"));
        loadNode("x", new XNode());
        loadNode("y", new YNode());
        loadNode("z", new ZNode());
        loadNode("br", new BrushNode());
        loadNode("brush", GlobalVars.operationParser.getOperator("br"));
    }

    private static void LoadFunction() {
        loadNode("%", new OddsNode());
        loadNode("odds", GlobalVars.operationParser.getOperator("%"));
        loadNode("chance", GlobalVars.operationParser.getOperator("%"));
        loadNode("-", new RangeNode());
        loadNode("range", GlobalVars.operationParser.getOperator("-"));
        loadNode("inrange", GlobalVars.operationParser.getOperator("-"));
        loadNode("in_range", GlobalVars.operationParser.getOperator("-"));
        loadNode(";", new RemainderNode());
        loadNode("rem", GlobalVars.operationParser.getOperator(";"));
        loadNode("remainder", GlobalVars.operationParser.getOperator(";"));
        loadNode("-con", new StringContainsNode());
        loadNode(";;", new EveryXNode());
        loadNode("every", GlobalVars.operationParser.getOperator(";;"));
        loadNode("step", GlobalVars.operationParser.getOperator(";;"));
        loadNode("#", new SimplexNode());
        loadNode("simplex", GlobalVars.operationParser.getOperator("#"));
        loadNode("##", new NoiseNode());
        loadNode("noise", GlobalVars.operationParser.getOperator("##"));
        loadNode("#a", new NoiseAtNode());
        loadNode("noiseat", GlobalVars.operationParser.getOperator("#a"));
        loadNode("fx", new FunctionNode());
        loadNode("function", GlobalVars.operationParser.getOperator("fx"));
        loadNode("m#", new MultiNoiseNode());
        loadNode("multinoise", GlobalVars.operationParser.getOperator("m#"));
    }

    private static void LoadLogical() {
        loadNode("&", new AndNode());
        loadNode("and", GlobalVars.operationParser.getOperator("&"));
        loadNode("both", GlobalVars.operationParser.getOperator("&"));
        loadNode("false", new FalseNode());
        loadNode("?", new IfNode());
        loadNode("if", GlobalVars.operationParser.getOperator("?"));
        loadNode(":", new ElseNode());
        loadNode("else", GlobalVars.operationParser.getOperator(":"));
        loadNode("~", new NotNode());
        loadNode("not", GlobalVars.operationParser.getOperator("~"));
        loadNode("|", new OrNode());
        loadNode("or", GlobalVars.operationParser.getOperator("|"));
        loadNode("either", GlobalVars.operationParser.getOperator("|"));
        loadNode("true", new TrueNode());
        loadNode("<", new XorNode());
        loadNode("xor", GlobalVars.operationParser.getOperator("<"));
        loadNode("one_of", GlobalVars.operationParser.getOperator("<"));
        loadNode("anyof", new AnyOfNode());
        loadNode("any_of", GlobalVars.operationParser.getOperator("anyof"));
    }

    private static void LoadLoop() {
        loadNode("-eq", new NumericEqualityNode());
        loadNode("-gte", new NumericGreaterEqualNode());
        loadNode("-gt", new NumericGreaterNode());
        loadNode("-lte", new NumericLessEqualNode());
        loadNode("-lt", new NumericLessNode());
        loadNode("loop", new WhileNode());
        loadNode("while", GlobalVars.operationParser.getOperator("loop"));
    }

    private static void LoadQuery() {
        loadNode("@", new BlocksAdjacentNode());
        loadNode("adj", GlobalVars.operationParser.getOperator("@"));
        loadNode("adjacent", GlobalVars.operationParser.getOperator("@"));
        loadNode("blocks_adjacent", GlobalVars.operationParser.getOperator("@"));
        loadNode("^", new BlocksAboveNode());
        loadNode("blocks_above", GlobalVars.operationParser.getOperator("^"));
        loadNode("above", GlobalVars.operationParser.getOperator("^"));
        loadNode("_", new BlocksBelowNode());
        loadNode("blocks_below", GlobalVars.operationParser.getOperator("_"));
        loadNode("below", GlobalVars.operationParser.getOperator("_"));
        loadNode("*", new FacesExposedNode());
        loadNode("exposed", GlobalVars.operationParser.getOperator("*"));
        loadNode("faces_exposed", GlobalVars.operationParser.getOperator("*"));
        loadNode("<<n", new GetNBTNode());
        loadNode("get_nbt", GlobalVars.operationParser.getOperator("<<n"));
        loadNode("at", new BlockAtNode());
        loadNode("block_at", GlobalVars.operationParser.getOperator("at"));
        loadNode("sky", new SkylightNode());
        loadNode("skylight", GlobalVars.operationParser.getOperator("sky"));
        loadNode("bl", new BlocklightNode());
        loadNode("blocklight", GlobalVars.operationParser.getOperator("bl"));
        loadNode("ang", new AngleNode());
        loadNode("angle", GlobalVars.operationParser.getOperator("ang"));
        loadNode("slope", GlobalVars.operationParser.getOperator("ang"));
        loadNode("biome_is", new CheckBiomeNode());
        loadNode("get_biome", GlobalVars.operationParser.getOperator("biome_is"));
        loadNode("is_biome", GlobalVars.operationParser.getOperator("biome_is"));
        loadNode("@@", new NearbyNode());
        loadNode("near", GlobalVars.operationParser.getOperator("@@"));
        loadNode("nearby", GlobalVars.operationParser.getOperator("@@"));
        loadNode("@v", new BlocksAdjacentVerticalNode());
        loadNode("adjv", GlobalVars.operationParser.getOperator("@v"));
        loadNode("adjacent_vertical", GlobalVars.operationParser.getOperator("@v"));
        loadNode("@h", new BlocksAdjacentHorizontalNode());
        loadNode("adjh", GlobalVars.operationParser.getOperator("@h"));
        loadNode("adjacent_horizontal", GlobalVars.operationParser.getOperator("@h"));
    }

    private static void LoadVariable() {
        loadNode("blk", new BlockVarNode());
        loadNode("block", GlobalVars.operationParser.getOperator("blk"));
        loadNode("dealloc", new DeallocNode());
        loadNode("deallocate", GlobalVars.operationParser.getOperator("dealloc"));
        loadNode("delete", GlobalVars.operationParser.getOperator("dealloc"));
        loadNode("delete_variable", GlobalVars.operationParser.getOperator("dealloc"));
        loadNode(">>i", new GetItemCommandNode());
        loadNode("get_item_command", GlobalVars.operationParser.getOperator(">>i"));
        loadNode("item_command", GlobalVars.operationParser.getOperator(">>i"));
        loadNode(">i", new GetItemVarNode());
        loadNode("get_item", GlobalVars.operationParser.getOperator(">i"));
        loadNode(">>m", new GetMonsterCommandNode());
        loadNode("get_monster_command", GlobalVars.operationParser.getOperator(">>m"));
        loadNode("monster_command", GlobalVars.operationParser.getOperator(">>m"));
        loadNode(">>s", new GetSpawnerCommandNode());
        loadNode("get_spawner_command", GlobalVars.operationParser.getOperator(">>s"));
        loadNode("spawner_command", GlobalVars.operationParser.getOperator(">>s"));
        loadNode("itm", new ItemVarNode());
        loadNode("item", GlobalVars.operationParser.getOperator("itm"));
        loadNode("mob", new MobVarNode());
        loadNode("monster", GlobalVars.operationParser.getOperator("mob"));
        loadNode("var", new ModifyVarNode());
        loadNode("modify_variable", GlobalVars.operationParser.getOperator("var"));
        loadNode("num", new NumericVarNode());
        loadNode("numeric", GlobalVars.operationParser.getOperator("num"));
        loadNode("number", GlobalVars.operationParser.getOperator("num"));
        loadNode("spwn", new SpawnerVarNode());
        loadNode("spawner_var", GlobalVars.operationParser.getOperator("spwn"));
        loadNode(">s", new SetSpawnerNode());
        loadNode("set_spawner", GlobalVars.operationParser.getOperator(">s"));
        loadNode(">m", new SpawnMonsterNode());
        loadNode("spawn_monster", GlobalVars.operationParser.getOperator(">m"));
        loadNode(">b", new SetBlockVarNode());
        loadNode("set_block_variable", GlobalVars.operationParser.getOperator(">b"));
        loadNode(">im", new GetMonsterItemNode());
        loadNode("monster_item", GlobalVars.operationParser.getOperator(">im"));
        loadNode("get_monster_item", GlobalVars.operationParser.getOperator(">im"));
        loadNode(">is", new GetSpawnerItemNode());
        loadNode("spawner_item", GlobalVars.operationParser.getOperator(">is"));
        loadNode("get_spawner_item", GlobalVars.operationParser.getOperator(">is"));
        loadNode("save", new SaveVariableNode());
        loadNode("load", new LoadVariableNode());
    }

    private static void LoadWorld() {
        loadNode("<<", new GetBlockDataNode());
        loadNode("get_data", GlobalVars.operationParser.getOperator("<<"));
        loadNode("data", GlobalVars.operationParser.getOperator("<<"));
        loadNode("get_block_data", GlobalVars.operationParser.getOperator("<<"));
        loadNode("!", new IgnorePhysicsNode());
        loadNode("physics", GlobalVars.operationParser.getOperator("!"));
        loadNode("ignore_physcics", GlobalVars.operationParser.getOperator("!"));
        loadNode("same", new SameNode());
        loadNode("facing", new FacingNode());
        loadNode(">>n", new SetNBTNode());
        loadNode("set_nbt", GlobalVars.operationParser.getOperator(">>n"));
        loadNode(">", new SetNode());
        loadNode("set", GlobalVars.operationParser.getOperator(">"));
        loadNode("set_block", GlobalVars.operationParser.getOperator(">"));
        loadNode(">>", GlobalVars.operationParser.getOperator(">"));
        loadNode("grav", new GravityNode());
        loadNode("gravity", GlobalVars.operationParser.getOperator("grav"));
        loadNode("biome", new SetBiomeNode());
        loadNode("set_biome", GlobalVars.operationParser.getOperator("biome"));
        loadNode("schem", new SchemBlockNode());
        loadNode("schematic", GlobalVars.operationParser.getOperator("schem"));
        loadNode(">r", new ReplaceNode());
        loadNode("replace", GlobalVars.operationParser.getOperator(">r"));
    }

    private static void LoadFun() {
        loadNode("smallruin", new SmallRuinNode());
        loadNode("template", new TemplateNode());
        loadNode("tpl", GlobalVars.operationParser.getOperator("template"));
    }
}
