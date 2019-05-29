package fourteener.worldeditor.operations;

import org.bukkit.block.Block;

import fourteener.worldeditor.operations.operators.EntryNode;

public class Operator {
	public static Operator currentOperator;
	public static Block currentBlock;
	
	public EntryNode entryNode;
	
	public boolean operateOnBlock (Block block) {
		// Set global operator variables
		currentOperator = this;
		currentBlock = block;
		
		// Perform the operation
		return entryNode.performNode();
	}
	
	public static Operator newOperator (String op) {
		Operator operator = new Operator();
		operator.entryNode = Parser.parseOperation(op);
		if (operator.entryNode == null) {
			return null;
		}
		return operator;
	}
}
