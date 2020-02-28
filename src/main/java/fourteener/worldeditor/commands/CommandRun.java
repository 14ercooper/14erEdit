package fourteener.worldeditor.commands;

import java.util.Arrays;

import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;

// These are dedicated versions of the undo and redo commands
public class CommandRun implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			String str = Arrays.toString(args);
			Operator op = Operator.newOperator(str);
			BlockState bs = Main.world.getBlockAt(((Player) sender).getLocation()).getState();
			op.operateOnBlock(bs, (Player) sender);
		}
		return false;
	}
}
