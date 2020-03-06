package fourteener.worldeditor.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.brush.Brush;
import fourteener.worldeditor.worldeditor.brush.BrushShape;

public class CommandRunat implements CommandExecutor {

	@SuppressWarnings("static-access")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Parse brushshape
		BrushShape brSh = Brush.GetBrushShape(args[3]);
		if (brSh == null) {
			return false;
		}
		int numArgs = (int) brSh.GetArgCount();
		List<Double> argList = new LinkedList<Double>();
		for (int i = 4; i <= numArgs + 3; i++) {
			argList.add(Double.parseDouble(args[i]));
		}
		List<Block> blocks = brSh.GetBlocks(argList,
				Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));

		// Parse the operator
		String opStr = "";
		for (int i = numArgs + 4; i < args.length; i++) {
			opStr = opStr.concat(args[i]).concat(" ");
		}
		Operator op = Operator.newOperator(opStr);

		// Operate on the brush selection
		for (Block b : blocks) {
			BlockState bs = b.getState();
			op.operateOnBlock(bs, (Player) sender);
			b.setType(op.currentBlock.getType());
			b.setBlockData(op.currentBlock.getBlockData());
		}
		return true;
	}
}
