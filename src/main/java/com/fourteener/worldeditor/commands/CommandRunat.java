package com.fourteener.worldeditor.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import com.fourteener.worldeditor.brush.Brush;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.operations.Operator;

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
		double x = 0, y = 0, z = 0;
		// X with relative
		if (args[0].contains("~")) {
			if (sender instanceof BlockCommandSender) {
				x = Double.parseDouble(args[0].replaceAll("~", "")) + ((BlockCommandSender) sender).getBlock().getX();
			}
			if (sender instanceof Entity) {
				x = Double.parseDouble(args[0].replaceAll("~", "")) + ((Entity) sender).getLocation().getX();
			}
		}
		else {
			x = Double.parseDouble(args[0]);
		}
		// Y with relative
		if (args[1].contains("~")) {
			if (sender instanceof BlockCommandSender) {
				y = Double.parseDouble(args[1].replaceAll("~", "")) + ((BlockCommandSender) sender).getBlock().getY();
			}
			if (sender instanceof Entity) {
				y = Double.parseDouble(args[1].replaceAll("~", "")) + ((Entity) sender).getLocation().getY();
			}
		}
		else {
			y = Double.parseDouble(args[1]);
		}
		// Z with relative
		if (args[0].contains("~")) {
			if (sender instanceof BlockCommandSender) {
				z = Double.parseDouble(args[2].replaceAll("~", "")) + ((BlockCommandSender) sender).getBlock().getZ();
			}
			if (sender instanceof Entity) {
				z = Double.parseDouble(args[2].replaceAll("~", "")) + ((Entity) sender).getLocation().getZ();
			}
		}
		else {
			z = Double.parseDouble(args[2]);
		}
		
		List<Block> blocks = brSh.GetBlocks(argList, x, y, z);

		// Parse the operator
		String opStr = "";
		for (int i = numArgs + 4; i < args.length; i++) {
			opStr = opStr.concat(args[i]).concat(" ");
		}
		Operator op = Operator.newOperator(opStr);

		// Operate on the brush selection
		List<BlockState> toOperate = new ArrayList<BlockState>();
		for (Block b : blocks) {
			toOperate.add(b.getState());
		}
		List<BlockState> operatedArray = new ArrayList<BlockState>();
		for (BlockState bs : toOperate) {
			op.operateOnBlock(bs);
			operatedArray.add(op.currentBlock);
		}
		for (BlockState bs : operatedArray) {
			Location l = bs.getLocation();
			Block b = GlobalVars.world.getBlockAt(l);
			b.setType(bs.getType(), Operator.ignoringPhysics);
			b.setBlockData(bs.getBlockData(), Operator.ignoringPhysics);
		}
		
		return true;
	}
}