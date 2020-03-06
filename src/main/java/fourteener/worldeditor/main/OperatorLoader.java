package fourteener.worldeditor.main;

import fourteener.worldeditor.operations.operators.core.*;
import fourteener.worldeditor.operations.operators.function.*;
import fourteener.worldeditor.operations.operators.logical.*;
import fourteener.worldeditor.operations.operators.loop.*;
import fourteener.worldeditor.operations.operators.query.*;
import fourteener.worldeditor.operations.operators.variable.*;
import fourteener.worldeditor.operations.operators.world.*;

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
		GlobalVars.operationParser.AddOperator("/", new LinkerNode());
		GlobalVars.operationParser.AddOperator(">f", new LoadFromFileNode());
		GlobalVars.operationParser.AddOperator("$", new MacroNode());
	}
	
	private static void LoadFunction() {
		GlobalVars.operationParser.AddOperator("%", new OddsNode());
		GlobalVars.operationParser.AddOperator("-", new RangeNode());
		GlobalVars.operationParser.AddOperator(";", new RemainderNode());
		GlobalVars.operationParser.AddOperator("#", new SimplexNode());
	}
	
	private static void LoadLogical() {
		GlobalVars.operationParser.AddOperator("&", new AndNode());
		GlobalVars.operationParser.AddOperator("false", new FalseNode());
		GlobalVars.operationParser.AddOperator("?", new IfNode());
		GlobalVars.operationParser.AddOperator("~", new NotNode());
		GlobalVars.operationParser.AddOperator("|", new OrNode());
		GlobalVars.operationParser.AddOperator("true", new TrueNode());
		GlobalVars.operationParser.AddOperator("<", new XorNode());
	}
	
	private static void LoadLoop() {
		GlobalVars.operationParser.AddOperator("-eq", new NumericEqualityNode());
		GlobalVars.operationParser.AddOperator("-gte", new NumericGreaterEqualNode());
		GlobalVars.operationParser.AddOperator("-gt", new NumericGreaterNode());
		GlobalVars.operationParser.AddOperator("-lte", new NumericLessEqualNode());
		GlobalVars.operationParser.AddOperator("-lt", new NumericLessNode());
		GlobalVars.operationParser.AddOperator("loop", new WhileNode());
	}
	
	private static void LoadQuery() {
		GlobalVars.operationParser.AddOperator("@", new BlocksAdjacentNode());
		GlobalVars.operationParser.AddOperator("^", new BlocksAboveNode());
		GlobalVars.operationParser.AddOperator("_", new BlocksBelowNode());
		GlobalVars.operationParser.AddOperator("*", new FacesExposedNode());
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
	}
	
	private static void LoadWorld() {
		GlobalVars.operationParser.AddOperator("<<", new GetBlockDataNode());
		GlobalVars.operationParser.AddOperator("!", new IgnorePhysicsNode());
		GlobalVars.operationParser.AddOperator("same", new SameNode());
		GlobalVars.operationParser.AddOperator(">>n", new SetNBTNode());
		GlobalVars.operationParser.AddOperator(">", new SetNode());
		GlobalVars.operationParser.AddOperator(">>", new SetPlusNode());
	}
}
