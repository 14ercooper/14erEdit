package com._14ercooper.worldeditor.selection;

import com._14ercooper.worldeditor.main.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SelectionWandListener implements Listener {

    // Helps to differentiate between different player's selections and prevents
    // duplication
    public static final List<SelectionWand> wands = new ArrayList<>();

    // If the player clicks, see if the selection needs updating
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Makes sure the player is holding a registered wand, else this doesn't need to
        // do anything
        Player p = event.getPlayer();
        boolean isValidPlayer;
        SelectionWand wand = null;
        // Check the player
        for (SelectionWand s : wands) {
            if (s.owner.equals(p.getUniqueId())) {
                wand = s;
                break;
            }
        }

        // Check the wand
        ItemStack itemStack = p.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        try {
            isValidPlayer = itemMeta.getDisplayName().equals(SelectionWand.wandName)
                    && itemStack.getType().equals(Material.WOODEN_AXE)
                    && itemMeta.getEnchants().containsKey(Enchantment.MENDING);
        } catch (NullPointerException e) {
            isValidPlayer = false;
        }

        // If the player isn't holding a valid wand, return without further action
        if (!isValidPlayer)
            return;

        // No manager, return with an error
        if (wand == null || wand.manager == null) {
            Main.logError("Could not use wand. Did you get a new wand after relogging?", p, null);
            return;
        }

        // Player left clicked, update position one
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block b = event.getClickedBlock();
            wand.manager.updatePositionOne(b.getX(), b.getY(), b.getZ(), p.getUniqueId());
            event.setCancelled(true);
        }

        // Player right clicked, update position two
        else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            EquipmentSlot e = event.getHand();
            // This if clause prevents the update from happening twice (due to off-hand
            // conflicts)
            if (e.equals(EquipmentSlot.HAND)) {
                Block b = event.getClickedBlock();
                wand.manager.updatePositionTwo(b.getX(), b.getY(), b.getZ(), p.getUniqueId());
                event.setCancelled(true);
            }
        }
    }
}
