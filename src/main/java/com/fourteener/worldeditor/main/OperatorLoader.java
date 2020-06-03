package com.fourteener.worldeditor.main;

import com.fourteener.worldeditor.operations.operators.core.*;
import com.fourteener.worldeditor.operations.operators.function.*;
import com.fourteener.worldeditor.operations.operators.logical.*;
import com.fourteener.worldeditor.operations.operators.loop.*;
import com.fourteener.worldeditor.operations.operators.query.*;
import com.fourteener.worldeditor.operations.operators.variable.*;
import com.fourteener.worldeditor.operations.operators.world.*;

public class OperatorLoader {
	public static void LoadOperators () {
		LoadCore();
		LoadFunction();
		LoadLogical();
		LoadLoop();
		LoadQuery();
		LoadVariable();
		LoadWorld();
	}
	
	private static void LoadCore() {
		GlobalVars.operationParser.AddOperator("$$", new CraftscriptNode());
		GlobalVars.operationParser.AddOperator("craftscript", GlobalVars.operationParser.GetOperator("$$"));
		GlobalVars.operationParser.AddOperator("/", new LinkerNode());
		GlobalVars.operationParser.AddOperator("link", GlobalVars.operationParser.GetOperator("/"));
		GlobalVars.operationParser.AddOperator(">f", new LoadFromFileNode());
		GlobalVars.operationParser.AddOperator("file", GlobalVars.operationParser.GetOperator(">f"));
		GlobalVars.operationParser.AddOperator("$", new MacroNode());
		GlobalVars.operationParser.AddOperator("macro", GlobalVars.operationParser.GetOperator("$"));
		GlobalVars.operationParser.AddOperator("x", new XNode());
		GlobalVars.operationParser.AddOperator("y", new YNode());
		GlobalVars.operationParser.AddOperator("z", new ZNode());
	}
	
	private static void LoadFunction() {
		GlobalVars.operationParser.AddOperator("%", new OddsNode());
		GlobalVars.operationParser.AddOperator("odds", GlobalVars.operationParser.GetOperator("%"));
		GlobalVars.operationParser.AddOperator("change", GlobalVars.operationParser.GetOperator("%"));
		GlobalVars.operationParser.AddOperator("-", new RangeNode());
		GlobalVars.operationParser.AddOperator("range", GlobalVars.operationParser.GetOperator("-"));
		GlobalVars.operationParser.AddOperator("inrange", GlobalVars.operationParser.GetOperator("-"));
		GlobalVars.operationParser.AddOperator("in_range", GlobalVars.operationParser.GetOperator("-"));
		GlobalVars.operationParser.AddOperator(";", new RemainderNode());
		GlobalVars.operationParser.AddOperator("rem", GlobalVars.operationParser.GetOperator(";"));
		GlobalVars.operationParser.AddOperator("remainder", GlobalVars.operationParser.GetOperator(";"));
		GlobalVars.operationParser.AddOperator("#", new SimplexNode());
		GlobalVars.operationParser.AddOperator("simplex", GlobalVars.operationParser.GetOperator("#"));
		GlobalVars.operationParser.AddOperator("-con", new StringContainsNode());
	}
	
	private static void LoadLogical() {
		GlobalVars.operationParser.AddOperator("&", new AndNode());
		GlobalVars.operationParser.AddOperator("and", GlobalVars.operationParser.GetOperator("&"));
		GlobalVars.operationParser.AddOperator("both", GlobalVars.operationParser.GetOperator("&"));
		GlobalVars.operationParser.AddOperator("false", new FalseNode());
		GlobalVars.operationParser.AddOperator("?", new IfNode());
		GlobalVars.operationParser.AddOperator("if", GlobalVars.operationParser.GetOperator("?"));
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
	}
	
	private static void LoadVariable() {
		GlobalVars.operationParser.AddOperator("blk", new BlockVarNode());
		GlobalVars.operationParser.AddOperator("dealloc", new DeallocNode());
		GlobalVars.operationParser.AddOperator(">>i", new GetItemCommandNode());
		GlobalVars.operationParser.AddOperator(">i", new GetItemVarNode());
		GlobalVars.operationParser.AddOperator(">>m", new GetMonsterCommandNode());
		GlobalVars.operationParser.AddOperator(">>s", new GetSpawnerCommandNode());
		GlobalVars.operationParser.AddOperator("itm", new ItemVarNode());
		GlobalVars.operationParser.AddOperator("mob", new MobVarNode());
		GlobalVars.operationParser.AddOperator("var", new ModifyVarNode());
		GlobalVars.operationParser.AddOperator("num", new NumericVarNode());
		GlobalVars.operationParser.AddOperator("spwn", new SpawnerVarNode());
		GlobalVars.operationParser.AddOperator(">s", new SetSpawnerNode());
		GlobalVars.operationParser.AddOperator(">m", new SpawnMonsterNode());
		GlobalVars.operationParser.AddOperator(">b", new SetBlockVarNode());
		GlobalVars.operationParser.AddOperator(">im", new GetMonsterItemNode());
		GlobalVars.operationParser.AddOperator(">is", new GetSpawnerItemNode());
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
		GlobalVars.operationParser.AddOperator("set_nbt", GlobalVars.operationParser.GetOperator(""));
		GlobalVars.operationParser.AddOperator(">", new SetNode());
		GlobalVars.operationParser.AddOperator("set", GlobalVars.operationParser.GetOperator(">"));
		GlobalVars.operationParser.AddOperator("set_block", GlobalVars.operationParser.GetOperator(">"));
		GlobalVars.operationParser.AddOperator(">>", new SetPlusNode());
		GlobalVars.operationParser.AddOperator("grav", new GravityNode());
	}
}
