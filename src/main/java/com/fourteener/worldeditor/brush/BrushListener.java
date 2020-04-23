package com.fourteener.worldeditor.brush;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import com.fourteener.worldeditor.main.GlobalVars;

public class BrushListener implements Listener {
	
	// Store all active brushes
	public static List<Brush> brushes = new ArrayList<Brush>();
	
	boolean dedupe = false;
	
	@EventHandler
	public void onPlayerInteract (PlayerInteractEvent event) {
		
		// Check the player is holding a brush
		// Do a quick check first (so this is a bit faster)
		if (event.getAction().equals(Action.PHYSICAL))
			return;
		
		// Then do a more detailed check
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		Brush brush = null;
		for (Brush b : brushes) {
			if (b.owner.equals(player) && b.item.equals(item)) {
				brush = b;
			}
		}
		if (brush == null)
			return;
		
		// The event has been verified, take control of it
		event.setCancelled(true);
		
		// Close to block deduplication
		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			return;
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
			if (!dedupe) {
				dedupe = true;
				return;
			}
			if (dedupe) {
				dedupe = false;
			}
		}
		
		// Then get the location where the brush should operate
		// This is a block where the player is looking, at a range no more than 256 blocks away
		Block block = getTargetBlock(player, 256);
		// If it's air, return
		if (block.getType().equals(Material.AIR))
			return;
		
		// And perform the operation there
		brush.operate(block.getX(), block.getY(), block.getZ());
	}

	private Block getTargetBlock(Player player, int range) {
		BlockIterator iter = new BlockIterator(player, range);
		Block lastBlock = iter.next();
		while (iter.hasNext()) {
			lastBlock = iter.next();
			
			if (GlobalVars.targetMask.contains(lastBlock.getType())) {
				continue;
			}
				
			break;
		}
		return lastBlock;
	}
}
