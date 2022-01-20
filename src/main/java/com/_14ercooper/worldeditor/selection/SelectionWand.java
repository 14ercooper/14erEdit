/**
 * This file is part of 14erEdit.
 * 
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.worldeditor.selection;

import com._14ercooper.worldeditor.player.PlayerManager;
import com._14ercooper.worldeditor.player.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class SelectionWand {
    public String owner; // Store the owner so people can have different selections
    public SelectionManager manager = new SelectionManager(); // Same reason as above

    public static final String wandName = "Selection Wand";

    // The wand has NBT so that it can be told apart from a standard axe
    // This determines if a new wand is needed, and if so, creates one
    public static SelectionWand giveNewWand(Player player) {
        PlayerWrapper playerWrapper = PlayerManager.INSTANCE.getPlayerWrapper(player);
        SelectionWand wand = playerWrapper.getSelectionWand();
        wand.givePlayerWand();
        return wand;
    }

    // This actually defines the wand and gives it to the player
    private void givePlayerWand() {
        ItemStack item = new ItemStack(Material.WOODEN_AXE); // Checked by the wand listener
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(wandName); // Checked by the wand listener
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.MENDING, 1); // Checked by the wand listener (though not the level)
        getOwner().getInventory().addItem(item);
    }

    public Player getOwner() {
        return Bukkit.getServer().getPlayer(UUID.fromString(owner));
    }
}
