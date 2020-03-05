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
		Main.operationParser.AddOperator("$$", new CraftscriptNode());
		Main.operationParser.AddOperator("/", new LinkerNode());
		Main.operationParser.AddOperator(">f", new LoadFromFileNode());
		Main.operationParser.AddOperator("$", new MacroNode());
	}
	
	private static void LoadFunction() {
		Main.operationParser.AddOperator("%", new OddsNode());
		Main.operationParser.AddOperator("-", new RangeNode());
		Main.operationParser.AddOperator(";", new RemainderNode());
		Main.operationParser.AddOperator("#", new SimplexNode());
	}
	
	private static void LoadLogical() {
		Main.operationParser.AddOperator("&", new AndNode());
		Main.operationParser.AddOperator("false", new FalseNode());
		Main.operationParser.AddOperator("?", new IfNode());
		Main.operationParser.AddOperator("~", new NotNode());
		Main.operationParser.AddOperator("|", new OrNode());
		Main.operationParser.AddOperator("true", new TrueNode());
		Main.operationParser.AddOperator("<", new XorNode());
	}
	
	private static void LoadLoop() {
		Main.operationParser.AddOperator("-eq", new NumericEqualityNode());
		Main.operationParser.AddOperator("-gte", new NumericGreaterEqualNode());
		Main.operationParser.AddOperator("-gt", new NumericGreaterNode());
		Main.operationParser.AddOperator("-lte", new NumericLessEqualNode());
		Main.operationParser.AddOperator("-lt", new NumericLessNode());
		Main.operationParser.AddOperator("loop", new WhileNode());
	}
	
	private static void LoadQuery() {
		Main.operationParser.AddOperator("@", new BlocksAdjacentNode());
		Main.operationParser.AddOperator("^", new BlocksAboveNode());
		Main.operationParser.AddOperator("_", new BlocksBelowNode());
		Main.operationParser.AddOperator("*", new FacesExposedNode());
	}
	
	private static void LoadVariable() {
		Main.operationParser.AddOperator("blk", new BlockVarNode());
		Main.operationParser.AddOperator("dealloc", new DeallocNode());
		Main.operationParser.AddOperator(">>i", new GetItemCommandNode());
		Main.operationParser.AddOperator(">i", new GetItemVarNode());
		Main.operationParser.AddOperator(">>m", new GetMonsterCommandNode());
		Main.operationParser.AddOperator(">>s", new GetSpawnerCommandNode());
		Main.operationParser.AddOperator("itm", new ItemVarNode());
		Main.operationParser.AddOperator("mob", new MobVarNode());
		Main.operationParser.AddOperator("var", new ModifyVarNode());
		Main.operationParser.AddOperator("num", new NumericVarNode());
		Main.operationParser.AddOperator("spwn", new SpawnerVarNode());
		Main.operationParser.AddOperator(">s", new SetSpawnerNode());
		Main.operationParser.AddOperator(">m", new SpawnMonsterNode());
		Main.operationParser.AddOperator(">b", new SetBlockVarNode());
	}
	
	private static void LoadWorld() {
		Main.operationParser.AddOperator("<<", new GetBlockDataNode());
		Main.operationParser.AddOperator("!", new IgnorePhysicsNode());
		Main.operationParser.AddOperator("same", new SameNode());
		Main.operationParser.AddOperator(">>n", new SetNBTNode());
		Main.operationParser.AddOperator(">", new SetNode());
		Main.operationParser.AddOperator(">>", new SetPlusNode());
	}
}
