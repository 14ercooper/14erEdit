package com._14ercooper.worldeditor.operations;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.operations.operators.core.BrushNode;
import com._14ercooper.worldeditor.operations.operators.core.CraftscriptNode;
import com._14ercooper.worldeditor.operations.operators.core.LinkerNode;
import com._14ercooper.worldeditor.operations.operators.core.LoadFromFileNode;
import com._14ercooper.worldeditor.operations.operators.core.MacroNode;
import com._14ercooper.worldeditor.operations.operators.core.TemplateNode;
import com._14ercooper.worldeditor.operations.operators.core.XNode;
import com._14ercooper.worldeditor.operations.operators.core.YNode;
import com._14ercooper.worldeditor.operations.operators.core.ZNode;
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
import com._14ercooper.worldeditor.operations.operators.world.FacingNode;
import com._14ercooper.worldeditor.operations.operators.world.GetBlockDataNode;
import com._14ercooper.worldeditor.operations.operators.world.GravityNode;
import com._14ercooper.worldeditor.operations.operators.world.IgnorePhysicsNode;
import com._14ercooper.worldeditor.operations.operators.world.ReplaceNode;
import com._14ercooper.worldeditor.operations.operators.world.SameNode;
import com._14ercooper.worldeditor.operations.operators.world.SchemBlockNode;
import com._14ercooper.worldeditor.operations.operators.world.SetBiomeNode;
import com._14ercooper.worldeditor.operations.operators.world.SetNBTNode;
import com._14ercooper.worldeditor.operations.operators.world.SetNode;

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

    private static void LoadCore() {
        GlobalVars.operationParser.addOperator("$$", new CraftscriptNode());
        GlobalVars.operationParser.addOperator("craftscript", GlobalVars.operationParser.getOperator("$$"));
        GlobalVars.operationParser.addOperator("script", GlobalVars.operationParser.getOperator("$$"));
        GlobalVars.operationParser.addOperator("/", new LinkerNode());
        GlobalVars.operationParser.addOperator("link", GlobalVars.operationParser.getOperator("/"));
        GlobalVars.operationParser.addOperator(">f", new LoadFromFileNode());
        GlobalVars.operationParser.addOperator("file", GlobalVars.operationParser.getOperator(">f"));
        GlobalVars.operationParser.addOperator("$", new MacroNode());
        GlobalVars.operationParser.addOperator("macro", GlobalVars.operationParser.getOperator("$"));
        GlobalVars.operationParser.addOperator("x", new XNode());
        GlobalVars.operationParser.addOperator("y", new YNode());
        GlobalVars.operationParser.addOperator("z", new ZNode());
        GlobalVars.operationParser.addOperator("br", new BrushNode());
        GlobalVars.operationParser.addOperator("brush", GlobalVars.operationParser.getOperator("br"));
    }

    private static void LoadFunction() {
        GlobalVars.operationParser.addOperator("%", new OddsNode());
        GlobalVars.operationParser.addOperator("odds", GlobalVars.operationParser.getOperator("%"));
        GlobalVars.operationParser.addOperator("chance", GlobalVars.operationParser.getOperator("%"));
        GlobalVars.operationParser.addOperator("-", new RangeNode());
        GlobalVars.operationParser.addOperator("range", GlobalVars.operationParser.getOperator("-"));
        GlobalVars.operationParser.addOperator("inrange", GlobalVars.operationParser.getOperator("-"));
        GlobalVars.operationParser.addOperator("in_range", GlobalVars.operationParser.getOperator("-"));
        GlobalVars.operationParser.addOperator(";", new RemainderNode());
        GlobalVars.operationParser.addOperator("rem", GlobalVars.operationParser.getOperator(";"));
        GlobalVars.operationParser.addOperator("remainder", GlobalVars.operationParser.getOperator(";"));
        GlobalVars.operationParser.addOperator("-con", new StringContainsNode());
        GlobalVars.operationParser.addOperator(";;", new EveryXNode());
        GlobalVars.operationParser.addOperator("every", GlobalVars.operationParser.getOperator(";;"));
        GlobalVars.operationParser.addOperator("step", GlobalVars.operationParser.getOperator(";;"));
        GlobalVars.operationParser.addOperator("#", new SimplexNode());
        GlobalVars.operationParser.addOperator("simplex", GlobalVars.operationParser.getOperator("#"));
        GlobalVars.operationParser.addOperator("##", new NoiseNode());
        GlobalVars.operationParser.addOperator("noise", GlobalVars.operationParser.getOperator("##"));
        GlobalVars.operationParser.addOperator("#a", new NoiseAtNode());
        GlobalVars.operationParser.addOperator("noiseat", GlobalVars.operationParser.getOperator("#a"));
        GlobalVars.operationParser.addOperator("fx", new FunctionNode());
        GlobalVars.operationParser.addOperator("function", GlobalVars.operationParser.getOperator("fx"));
        GlobalVars.operationParser.addOperator("m#", new MultiNoiseNode());
        GlobalVars.operationParser.addOperator("multinoise", GlobalVars.operationParser.getOperator("m#"));
    }

    private static void LoadLogical() {
        GlobalVars.operationParser.addOperator("&", new AndNode());
        GlobalVars.operationParser.addOperator("and", GlobalVars.operationParser.getOperator("&"));
        GlobalVars.operationParser.addOperator("both", GlobalVars.operationParser.getOperator("&"));
        GlobalVars.operationParser.addOperator("false", new FalseNode());
        GlobalVars.operationParser.addOperator("?", new IfNode());
        GlobalVars.operationParser.addOperator("if", GlobalVars.operationParser.getOperator("?"));
        GlobalVars.operationParser.addOperator(":", new ElseNode());
        GlobalVars.operationParser.addOperator("else", GlobalVars.operationParser.getOperator(":"));
        GlobalVars.operationParser.addOperator("~", new NotNode());
        GlobalVars.operationParser.addOperator("not", GlobalVars.operationParser.getOperator("~"));
        GlobalVars.operationParser.addOperator("|", new OrNode());
        GlobalVars.operationParser.addOperator("or", GlobalVars.operationParser.getOperator("|"));
        GlobalVars.operationParser.addOperator("either", GlobalVars.operationParser.getOperator("|"));
        GlobalVars.operationParser.addOperator("true", new TrueNode());
        GlobalVars.operationParser.addOperator("<", new XorNode());
        GlobalVars.operationParser.addOperator("xor", GlobalVars.operationParser.getOperator("<"));
        GlobalVars.operationParser.addOperator("one_of", GlobalVars.operationParser.getOperator("<"));
        GlobalVars.operationParser.addOperator("anyof", new AnyOfNode());
        GlobalVars.operationParser.addOperator("any_of", GlobalVars.operationParser.getOperator("anyof"));
    }

    private static void LoadLoop() {
        GlobalVars.operationParser.addOperator("-eq", new NumericEqualityNode());
        GlobalVars.operationParser.addOperator("-gte", new NumericGreaterEqualNode());
        GlobalVars.operationParser.addOperator("-gt", new NumericGreaterNode());
        GlobalVars.operationParser.addOperator("-lte", new NumericLessEqualNode());
        GlobalVars.operationParser.addOperator("-lt", new NumericLessNode());
        GlobalVars.operationParser.addOperator("loop", new WhileNode());
        GlobalVars.operationParser.addOperator("while", GlobalVars.operationParser.getOperator("loop"));
    }

    private static void LoadQuery() {
        GlobalVars.operationParser.addOperator("@", new BlocksAdjacentNode());
        GlobalVars.operationParser.addOperator("adj", GlobalVars.operationParser.getOperator("@"));
        GlobalVars.operationParser.addOperator("adjacent", GlobalVars.operationParser.getOperator("@"));
        GlobalVars.operationParser.addOperator("blocks_adjacent", GlobalVars.operationParser.getOperator("@"));
        GlobalVars.operationParser.addOperator("^", new BlocksAboveNode());
        GlobalVars.operationParser.addOperator("blocks_above", GlobalVars.operationParser.getOperator("^"));
        GlobalVars.operationParser.addOperator("above", GlobalVars.operationParser.getOperator("^"));
        GlobalVars.operationParser.addOperator("_", new BlocksBelowNode());
        GlobalVars.operationParser.addOperator("blocks_below", GlobalVars.operationParser.getOperator("_"));
        GlobalVars.operationParser.addOperator("below", GlobalVars.operationParser.getOperator("_"));
        GlobalVars.operationParser.addOperator("*", new FacesExposedNode());
        GlobalVars.operationParser.addOperator("exposed", GlobalVars.operationParser.getOperator("*"));
        GlobalVars.operationParser.addOperator("faces_exposed", GlobalVars.operationParser.getOperator("*"));
        GlobalVars.operationParser.addOperator("<<n", new GetNBTNode());
        GlobalVars.operationParser.addOperator("get_nbt", GlobalVars.operationParser.getOperator("<<n"));
        GlobalVars.operationParser.addOperator("at", new BlockAtNode());
        GlobalVars.operationParser.addOperator("block_at", GlobalVars.operationParser.getOperator("at"));
        GlobalVars.operationParser.addOperator("sky", new SkylightNode());
        GlobalVars.operationParser.addOperator("skylight", GlobalVars.operationParser.getOperator("sky"));
        GlobalVars.operationParser.addOperator("bl", new BlocklightNode());
        GlobalVars.operationParser.addOperator("blocklight", GlobalVars.operationParser.getOperator("bl"));
        GlobalVars.operationParser.addOperator("ang", new AngleNode());
        GlobalVars.operationParser.addOperator("angle", GlobalVars.operationParser.getOperator("ang"));
        GlobalVars.operationParser.addOperator("slope", GlobalVars.operationParser.getOperator("ang"));
        GlobalVars.operationParser.addOperator("biome_is", new CheckBiomeNode());
        GlobalVars.operationParser.addOperator("get_biome", GlobalVars.operationParser.getOperator("biome_is"));
        GlobalVars.operationParser.addOperator("is_biome", GlobalVars.operationParser.getOperator("biome_is"));
        GlobalVars.operationParser.addOperator("@@", new NearbyNode());
        GlobalVars.operationParser.addOperator("near", GlobalVars.operationParser.getOperator("@@"));
        GlobalVars.operationParser.addOperator("nearby", GlobalVars.operationParser.getOperator("@@"));
        GlobalVars.operationParser.addOperator("@v", new BlocksAdjacentVerticalNode());
        GlobalVars.operationParser.addOperator("adjv", GlobalVars.operationParser.getOperator("@v"));
        GlobalVars.operationParser.addOperator("adjacent_vertical", GlobalVars.operationParser.getOperator("@v"));
        GlobalVars.operationParser.addOperator("@h", new BlocksAdjacentHorizontalNode());
        GlobalVars.operationParser.addOperator("adjh", GlobalVars.operationParser.getOperator("@h"));
        GlobalVars.operationParser.addOperator("adjacent_horizontal", GlobalVars.operationParser.getOperator("@h"));
    }

    private static void LoadVariable() {
        GlobalVars.operationParser.addOperator("blk", new BlockVarNode());
        GlobalVars.operationParser.addOperator("block", GlobalVars.operationParser.getOperator("blk"));
        GlobalVars.operationParser.addOperator("dealloc", new DeallocNode());
        GlobalVars.operationParser.addOperator("deallocate", GlobalVars.operationParser.getOperator("dealloc"));
        GlobalVars.operationParser.addOperator("delete", GlobalVars.operationParser.getOperator("dealloc"));
        GlobalVars.operationParser.addOperator("delete_variable", GlobalVars.operationParser.getOperator("dealloc"));
        GlobalVars.operationParser.addOperator(">>i", new GetItemCommandNode());
        GlobalVars.operationParser.addOperator("get_item_command", GlobalVars.operationParser.getOperator(">>i"));
        GlobalVars.operationParser.addOperator("item_command", GlobalVars.operationParser.getOperator(">>i"));
        GlobalVars.operationParser.addOperator(">i", new GetItemVarNode());
        GlobalVars.operationParser.addOperator("get_item", GlobalVars.operationParser.getOperator(">i"));
        GlobalVars.operationParser.addOperator(">>m", new GetMonsterCommandNode());
        GlobalVars.operationParser.addOperator("get_monster_command", GlobalVars.operationParser.getOperator(">>m"));
        GlobalVars.operationParser.addOperator("monster_command", GlobalVars.operationParser.getOperator(">>m"));
        GlobalVars.operationParser.addOperator(">>s", new GetSpawnerCommandNode());
        GlobalVars.operationParser.addOperator("get_spawner_command", GlobalVars.operationParser.getOperator(">>s"));
        GlobalVars.operationParser.addOperator("spawner_command", GlobalVars.operationParser.getOperator(">>s"));
        GlobalVars.operationParser.addOperator("itm", new ItemVarNode());
        GlobalVars.operationParser.addOperator("item", GlobalVars.operationParser.getOperator("itm"));
        GlobalVars.operationParser.addOperator("mob", new MobVarNode());
        GlobalVars.operationParser.addOperator("monster", GlobalVars.operationParser.getOperator("mob"));
        GlobalVars.operationParser.addOperator("var", new ModifyVarNode());
        GlobalVars.operationParser.addOperator("modify_variable", GlobalVars.operationParser.getOperator("var"));
        GlobalVars.operationParser.addOperator("num", new NumericVarNode());
        GlobalVars.operationParser.addOperator("numeric", GlobalVars.operationParser.getOperator("num"));
        GlobalVars.operationParser.addOperator("number", GlobalVars.operationParser.getOperator("num"));
        GlobalVars.operationParser.addOperator("spwn", new SpawnerVarNode());
        GlobalVars.operationParser.addOperator("spawner_var", GlobalVars.operationParser.getOperator("spwn"));
        GlobalVars.operationParser.addOperator(">s", new SetSpawnerNode());
        GlobalVars.operationParser.addOperator("set_spawner", GlobalVars.operationParser.getOperator(">s"));
        GlobalVars.operationParser.addOperator(">m", new SpawnMonsterNode());
        GlobalVars.operationParser.addOperator("spawn_monster", GlobalVars.operationParser.getOperator(">m"));
        GlobalVars.operationParser.addOperator(">b", new SetBlockVarNode());
        GlobalVars.operationParser.addOperator("set_block_variable", GlobalVars.operationParser.getOperator(">b"));
        GlobalVars.operationParser.addOperator(">im", new GetMonsterItemNode());
        GlobalVars.operationParser.addOperator("monster_item", GlobalVars.operationParser.getOperator(">im"));
        GlobalVars.operationParser.addOperator("get_monster_item", GlobalVars.operationParser.getOperator(">im"));
        GlobalVars.operationParser.addOperator(">is", new GetSpawnerItemNode());
        GlobalVars.operationParser.addOperator("spawner_item", GlobalVars.operationParser.getOperator(">is"));
        GlobalVars.operationParser.addOperator("get_spawner_item", GlobalVars.operationParser.getOperator(">is"));
        GlobalVars.operationParser.addOperator("save", new SaveVariableNode());
        GlobalVars.operationParser.addOperator("load", new LoadVariableNode());
    }

    private static void LoadWorld() {
        GlobalVars.operationParser.addOperator("<<", new GetBlockDataNode());
        GlobalVars.operationParser.addOperator("get_data", GlobalVars.operationParser.getOperator("<<"));
        GlobalVars.operationParser.addOperator("data", GlobalVars.operationParser.getOperator("<<"));
        GlobalVars.operationParser.addOperator("get_block_data", GlobalVars.operationParser.getOperator("<<"));
        GlobalVars.operationParser.addOperator("!", new IgnorePhysicsNode());
        GlobalVars.operationParser.addOperator("physics", GlobalVars.operationParser.getOperator("!"));
        GlobalVars.operationParser.addOperator("ignore_physcics", GlobalVars.operationParser.getOperator("!"));
        GlobalVars.operationParser.addOperator("same", new SameNode());
        GlobalVars.operationParser.addOperator("facing", new FacingNode());
        GlobalVars.operationParser.addOperator(">>n", new SetNBTNode());
        GlobalVars.operationParser.addOperator("set_nbt", GlobalVars.operationParser.getOperator(">>n"));
        GlobalVars.operationParser.addOperator(">", new SetNode());
        GlobalVars.operationParser.addOperator("set", GlobalVars.operationParser.getOperator(">"));
        GlobalVars.operationParser.addOperator("set_block", GlobalVars.operationParser.getOperator(">"));
        GlobalVars.operationParser.addOperator(">>", GlobalVars.operationParser.getOperator(">"));
        GlobalVars.operationParser.addOperator("grav", new GravityNode());
        GlobalVars.operationParser.addOperator("gravity", GlobalVars.operationParser.getOperator("grav"));
        GlobalVars.operationParser.addOperator("biome", new SetBiomeNode());
        GlobalVars.operationParser.addOperator("set_biome", GlobalVars.operationParser.getOperator("biome"));
        GlobalVars.operationParser.addOperator("schem", new SchemBlockNode());
        GlobalVars.operationParser.addOperator("schematic", GlobalVars.operationParser.getOperator("schem"));
        GlobalVars.operationParser.addOperator(">r", new ReplaceNode());
        GlobalVars.operationParser.addOperator("replace", GlobalVars.operationParser.getOperator(">r"));
    }

    private static void LoadFun() {
        GlobalVars.operationParser.addOperator("smallruin", new SmallRuinNode());
        GlobalVars.operationParser.addOperator("template", new TemplateNode());
        GlobalVars.operationParser.addOperator("tpl", GlobalVars.operationParser.getOperator("template"));
    }
}
