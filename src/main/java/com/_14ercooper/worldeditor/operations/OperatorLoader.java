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
import com._14ercooper.worldeditor.operations.operators.query.BlocksAdjacentNode;
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
	GlobalVars.operationParser.AddOperator("$$", new CraftscriptNode());
	GlobalVars.operationParser.AddOperator("craftscript", GlobalVars.operationParser.GetOperator("$$"));
	GlobalVars.operationParser.AddOperator("script", GlobalVars.operationParser.GetOperator("$$"));
	GlobalVars.operationParser.AddOperator("/", new LinkerNode());
	GlobalVars.operationParser.AddOperator("link", GlobalVars.operationParser.GetOperator("/"));
	GlobalVars.operationParser.AddOperator(">f", new LoadFromFileNode());
	GlobalVars.operationParser.AddOperator("file", GlobalVars.operationParser.GetOperator(">f"));
	GlobalVars.operationParser.AddOperator("$", new MacroNode());
	GlobalVars.operationParser.AddOperator("macro", GlobalVars.operationParser.GetOperator("$"));
	GlobalVars.operationParser.AddOperator("x", new XNode());
	GlobalVars.operationParser.AddOperator("y", new YNode());
	GlobalVars.operationParser.AddOperator("z", new ZNode());
	GlobalVars.operationParser.AddOperator("br", new BrushNode());
	GlobalVars.operationParser.AddOperator("brush", GlobalVars.operationParser.GetOperator("br"));
    }

    private static void LoadFunction() {
	GlobalVars.operationParser.AddOperator("%", new OddsNode());
	GlobalVars.operationParser.AddOperator("odds", GlobalVars.operationParser.GetOperator("%"));
	GlobalVars.operationParser.AddOperator("chance", GlobalVars.operationParser.GetOperator("%"));
	GlobalVars.operationParser.AddOperator("-", new RangeNode());
	GlobalVars.operationParser.AddOperator("range", GlobalVars.operationParser.GetOperator("-"));
	GlobalVars.operationParser.AddOperator("inrange", GlobalVars.operationParser.GetOperator("-"));
	GlobalVars.operationParser.AddOperator("in_range", GlobalVars.operationParser.GetOperator("-"));
	GlobalVars.operationParser.AddOperator(";", new RemainderNode());
	GlobalVars.operationParser.AddOperator("rem", GlobalVars.operationParser.GetOperator(";"));
	GlobalVars.operationParser.AddOperator("remainder", GlobalVars.operationParser.GetOperator(";"));
	GlobalVars.operationParser.AddOperator("-con", new StringContainsNode());
	GlobalVars.operationParser.AddOperator(";;", new EveryXNode());
	GlobalVars.operationParser.AddOperator("every", GlobalVars.operationParser.GetOperator(";;"));
	GlobalVars.operationParser.AddOperator("step", GlobalVars.operationParser.GetOperator(";;"));
	GlobalVars.operationParser.AddOperator("#", new SimplexNode());
	GlobalVars.operationParser.AddOperator("simplex", GlobalVars.operationParser.GetOperator("#"));
	GlobalVars.operationParser.AddOperator("##", new NoiseNode());
	GlobalVars.operationParser.AddOperator("noise", GlobalVars.operationParser.GetOperator("##"));
	GlobalVars.operationParser.AddOperator("#a", new NoiseAtNode());
	GlobalVars.operationParser.AddOperator("noiseat", GlobalVars.operationParser.GetOperator("#a"));
	GlobalVars.operationParser.AddOperator("fx", new FunctionNode());
	GlobalVars.operationParser.AddOperator("function", GlobalVars.operationParser.GetOperator("fx"));
	GlobalVars.operationParser.AddOperator("m#", new MultiNoiseNode());
	GlobalVars.operationParser.AddOperator("multinoise", GlobalVars.operationParser.GetOperator("m#"));
    }

    private static void LoadLogical() {
	GlobalVars.operationParser.AddOperator("&", new AndNode());
	GlobalVars.operationParser.AddOperator("and", GlobalVars.operationParser.GetOperator("&"));
	GlobalVars.operationParser.AddOperator("both", GlobalVars.operationParser.GetOperator("&"));
	GlobalVars.operationParser.AddOperator("false", new FalseNode());
	GlobalVars.operationParser.AddOperator("?", new IfNode());
	GlobalVars.operationParser.AddOperator("if", GlobalVars.operationParser.GetOperator("?"));
	GlobalVars.operationParser.AddOperator(":", new ElseNode());
	GlobalVars.operationParser.AddOperator("else", GlobalVars.operationParser.GetOperator(":"));
	GlobalVars.operationParser.AddOperator("~", new NotNode());
	GlobalVars.operationParser.AddOperator("not", GlobalVars.operationParser.GetOperator("~"));
	GlobalVars.operationParser.AddOperator("|", new OrNode());
	GlobalVars.operationParser.AddOperator("or", GlobalVars.operationParser.GetOperator("|"));
	GlobalVars.operationParser.AddOperator("either", GlobalVars.operationParser.GetOperator("|"));
	GlobalVars.operationParser.AddOperator("true", new TrueNode());
	GlobalVars.operationParser.AddOperator("<", new XorNode());
	GlobalVars.operationParser.AddOperator("xor", GlobalVars.operationParser.GetOperator("<"));
	GlobalVars.operationParser.AddOperator("one_of", GlobalVars.operationParser.GetOperator("<"));
    }

    private static void LoadLoop() {
	GlobalVars.operationParser.AddOperator("-eq", new NumericEqualityNode());
	GlobalVars.operationParser.AddOperator("-gte", new NumericGreaterEqualNode());
	GlobalVars.operationParser.AddOperator("-gt", new NumericGreaterNode());
	GlobalVars.operationParser.AddOperator("-lte", new NumericLessEqualNode());
	GlobalVars.operationParser.AddOperator("-lt", new NumericLessNode());
	GlobalVars.operationParser.AddOperator("loop", new WhileNode());
	GlobalVars.operationParser.AddOperator("while", GlobalVars.operationParser.GetOperator("loop"));
    }

    private static void LoadQuery() {
	GlobalVars.operationParser.AddOperator("@", new BlocksAdjacentNode());
	GlobalVars.operationParser.AddOperator("adj", GlobalVars.operationParser.GetOperator("@"));
	GlobalVars.operationParser.AddOperator("adjacent", GlobalVars.operationParser.GetOperator("@"));
	GlobalVars.operationParser.AddOperator("blocks_adjacent", GlobalVars.operationParser.GetOperator("@"));
	GlobalVars.operationParser.AddOperator("^", new BlocksAboveNode());
	GlobalVars.operationParser.AddOperator("blocks_above", GlobalVars.operationParser.GetOperator("^"));
	GlobalVars.operationParser.AddOperator("above", GlobalVars.operationParser.GetOperator("^"));
	GlobalVars.operationParser.AddOperator("_", new BlocksBelowNode());
	GlobalVars.operationParser.AddOperator("blocks_below", GlobalVars.operationParser.GetOperator("_"));
	GlobalVars.operationParser.AddOperator("below", GlobalVars.operationParser.GetOperator("_"));
	GlobalVars.operationParser.AddOperator("*", new FacesExposedNode());
	GlobalVars.operationParser.AddOperator("exposed", GlobalVars.operationParser.GetOperator("*"));
	GlobalVars.operationParser.AddOperator("faces_exposed", GlobalVars.operationParser.GetOperator("*"));
	GlobalVars.operationParser.AddOperator("<<n", new GetNBTNode());
	GlobalVars.operationParser.AddOperator("get_nbt", GlobalVars.operationParser.GetOperator("<<n"));
	GlobalVars.operationParser.AddOperator("at", new BlockAtNode());
	GlobalVars.operationParser.AddOperator("block_at", GlobalVars.operationParser.GetOperator("at"));
	GlobalVars.operationParser.AddOperator("sky", new SkylightNode());
	GlobalVars.operationParser.AddOperator("skylight", GlobalVars.operationParser.GetOperator("sky"));
	GlobalVars.operationParser.AddOperator("bl", new BlocklightNode());
	GlobalVars.operationParser.AddOperator("blocklight", GlobalVars.operationParser.GetOperator("bl"));
	GlobalVars.operationParser.AddOperator("ang", new AngleNode());
	GlobalVars.operationParser.AddOperator("angle", GlobalVars.operationParser.GetOperator("ang"));
	GlobalVars.operationParser.AddOperator("slope", GlobalVars.operationParser.GetOperator("ang"));
	GlobalVars.operationParser.AddOperator("biome_is", new CheckBiomeNode());
	GlobalVars.operationParser.AddOperator("get_biome", GlobalVars.operationParser.GetOperator("biome_is"));
	GlobalVars.operationParser.AddOperator("@@", new NearbyNode());
	GlobalVars.operationParser.AddOperator("near", GlobalVars.operationParser.GetOperator("@@"));
	GlobalVars.operationParser.AddOperator("nearby", GlobalVars.operationParser.GetOperator("@@"));
    }

    private static void LoadVariable() {
	GlobalVars.operationParser.AddOperator("blk", new BlockVarNode());
	GlobalVars.operationParser.AddOperator("block", GlobalVars.operationParser.GetOperator("blk"));
	GlobalVars.operationParser.AddOperator("dealloc", new DeallocNode());
	GlobalVars.operationParser.AddOperator("deallocate", GlobalVars.operationParser.GetOperator("dealloc"));
	GlobalVars.operationParser.AddOperator("delete", GlobalVars.operationParser.GetOperator("dealloc"));
	GlobalVars.operationParser.AddOperator("delete_variable", GlobalVars.operationParser.GetOperator("dealloc"));
	GlobalVars.operationParser.AddOperator(">>i", new GetItemCommandNode());
	GlobalVars.operationParser.AddOperator("get_item_command", GlobalVars.operationParser.GetOperator(">>i"));
	GlobalVars.operationParser.AddOperator("item_command", GlobalVars.operationParser.GetOperator(">>i"));
	GlobalVars.operationParser.AddOperator(">i", new GetItemVarNode());
	GlobalVars.operationParser.AddOperator("get_item", GlobalVars.operationParser.GetOperator(">i"));
	GlobalVars.operationParser.AddOperator(">>m", new GetMonsterCommandNode());
	GlobalVars.operationParser.AddOperator("get_monster_command", GlobalVars.operationParser.GetOperator(">>m"));
	GlobalVars.operationParser.AddOperator("monster_command", GlobalVars.operationParser.GetOperator(">>m"));
	GlobalVars.operationParser.AddOperator(">>s", new GetSpawnerCommandNode());
	GlobalVars.operationParser.AddOperator("get_spawner_command", GlobalVars.operationParser.GetOperator(">>s"));
	GlobalVars.operationParser.AddOperator("spawner_command", GlobalVars.operationParser.GetOperator(">>s"));
	GlobalVars.operationParser.AddOperator("itm", new ItemVarNode());
	GlobalVars.operationParser.AddOperator("item", GlobalVars.operationParser.GetOperator("itm"));
	GlobalVars.operationParser.AddOperator("mob", new MobVarNode());
	GlobalVars.operationParser.AddOperator("monster", GlobalVars.operationParser.GetOperator("mob"));
	GlobalVars.operationParser.AddOperator("var", new ModifyVarNode());
	GlobalVars.operationParser.AddOperator("modify_variable", GlobalVars.operationParser.GetOperator("var"));
	GlobalVars.operationParser.AddOperator("num", new NumericVarNode());
	GlobalVars.operationParser.AddOperator("numeric", GlobalVars.operationParser.GetOperator("num"));
	GlobalVars.operationParser.AddOperator("number", GlobalVars.operationParser.GetOperator("num"));
	GlobalVars.operationParser.AddOperator("spwn", new SpawnerVarNode());
	GlobalVars.operationParser.AddOperator("spawner_var", GlobalVars.operationParser.GetOperator("spwn"));
	GlobalVars.operationParser.AddOperator(">s", new SetSpawnerNode());
	GlobalVars.operationParser.AddOperator("set_spawner", GlobalVars.operationParser.GetOperator(">s"));
	GlobalVars.operationParser.AddOperator(">m", new SpawnMonsterNode());
	GlobalVars.operationParser.AddOperator("spawn_monster", GlobalVars.operationParser.GetOperator(">m"));
	GlobalVars.operationParser.AddOperator(">b", new SetBlockVarNode());
	GlobalVars.operationParser.AddOperator("set_block_variable", GlobalVars.operationParser.GetOperator(">b"));
	GlobalVars.operationParser.AddOperator(">im", new GetMonsterItemNode());
	GlobalVars.operationParser.AddOperator("monster_item", GlobalVars.operationParser.GetOperator(">im"));
	GlobalVars.operationParser.AddOperator("get_monster_item", GlobalVars.operationParser.GetOperator(">im"));
	GlobalVars.operationParser.AddOperator(">is", new GetSpawnerItemNode());
	GlobalVars.operationParser.AddOperator("spawner_item", GlobalVars.operationParser.GetOperator(">is"));
	GlobalVars.operationParser.AddOperator("get_spawner_item", GlobalVars.operationParser.GetOperator(">is"));
	GlobalVars.operationParser.AddOperator("save", new SaveVariableNode());
	GlobalVars.operationParser.AddOperator("load", new LoadVariableNode());
    }

    private static void LoadWorld() {
	GlobalVars.operationParser.AddOperator("<<", new GetBlockDataNode());
	GlobalVars.operationParser.AddOperator("get_data", GlobalVars.operationParser.GetOperator("<<"));
	GlobalVars.operationParser.AddOperator("data", GlobalVars.operationParser.GetOperator("<<"));
	GlobalVars.operationParser.AddOperator("get_block_data", GlobalVars.operationParser.GetOperator("<<"));
	GlobalVars.operationParser.AddOperator("!", new IgnorePhysicsNode());
	GlobalVars.operationParser.AddOperator("physics", GlobalVars.operationParser.GetOperator("!"));
	GlobalVars.operationParser.AddOperator("ignore_physcics", GlobalVars.operationParser.GetOperator("!"));
	GlobalVars.operationParser.AddOperator("same", new SameNode());
	GlobalVars.operationParser.AddOperator("facing", new FacingNode());
	GlobalVars.operationParser.AddOperator(">>n", new SetNBTNode());
	GlobalVars.operationParser.AddOperator("set_nbt", GlobalVars.operationParser.GetOperator(">>n"));
	GlobalVars.operationParser.AddOperator(">", new SetNode());
	GlobalVars.operationParser.AddOperator("set", GlobalVars.operationParser.GetOperator(">"));
	GlobalVars.operationParser.AddOperator("set_block", GlobalVars.operationParser.GetOperator(">"));
	GlobalVars.operationParser.AddOperator(">>", GlobalVars.operationParser.GetOperator(">"));
	GlobalVars.operationParser.AddOperator("grav", new GravityNode());
	GlobalVars.operationParser.AddOperator("gravity", GlobalVars.operationParser.GetOperator("grav"));
	GlobalVars.operationParser.AddOperator("biome", new SetBiomeNode());
	GlobalVars.operationParser.AddOperator("set_biome", GlobalVars.operationParser.GetOperator("biome"));
	GlobalVars.operationParser.AddOperator("schem", new SchemBlockNode());
	GlobalVars.operationParser.AddOperator("schematic", GlobalVars.operationParser.GetOperator("schem"));
    }

    private static void LoadFun() {
	GlobalVars.operationParser.AddOperator("smallruin", new SmallRuinNode());
	GlobalVars.operationParser.AddOperator("template", new TemplateNode());
	GlobalVars.operationParser.AddOperator("tpl", GlobalVars.operationParser.GetOperator("template"));
    }
}
