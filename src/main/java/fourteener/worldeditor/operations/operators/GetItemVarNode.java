package fourteener.worldeditor.operations.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.type.ItemVar;
import net.md_5.bungee.api.ChatColor;

public class GetItemVarNode extends Node {
	
	String name;
	
	public static GetItemVarNode newNode (String val) {
		GetItemVarNode node =  new GetItemVarNode();
		node.name = val;
		return node;
	}
	
	public boolean performNode () {
		ItemVar iv = Operator.itemVars.get(name);
		ItemStack is = new ItemStack(Material.matchMaterial(iv.getType()));
		ItemMeta im = is.getItemMeta();
		if (!iv.getName().isEmpty()) {
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', iv.getName().replace('_', ' ')));
		}
		List<String> colorLore = new ArrayList<String>();
		for (String s : iv.getLore()) {
			colorLore.add(ChatColor.translateAlternateColorCodes('&', s.replace('_', ' ')));
		}
		if (colorLore.size() > 0) {
			im.setLore(colorLore);
		}
		for (Map.Entry<String, Integer> entry : iv.getEnchants().entrySet()) {
			im.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(entry.getKey())), entry.getValue(), true);
		}
		for (Map.Entry<String, String> entry : iv.getAttributes().entrySet()) {
			String[] mod = entry.getValue().split("\\s+");
			im.addAttributeModifier(Attribute.valueOf(entry.getKey()), // op slot value
					new AttributeModifier(UUID.randomUUID(), entry.getKey(), Double.parseDouble(mod[2]), AttributeModifier.Operation.valueOf(mod[0]), EquipmentSlot.valueOf(mod[1])));
		}
		is.setAmount(iv.getCount());
		for (String s : iv.getFlags()) {
			im.addItemFlags(ItemFlag.valueOf(s));
		}
		is.setItemMeta(im);
		Operator.currentPlayer.getInventory().addItem(is);
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
