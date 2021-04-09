package com._14ercooper.worldeditor.operations.type;

import com._14ercooper.worldeditor.main.GlobalVars;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemVar implements Serializable {
    private static final long serialVersionUID = 1L;

    String type = "";
    ColorText name = new ColorText();
    List<ColorText> lore = new ArrayList<>();
    Map<String, Integer> enchants = new HashMap<>();
    Map<String, String> attributes = new HashMap<>();
    int count = 1;
    int flags = 0;
    int damage = 0;
    boolean unbreakable = false;

    // Get the item's NBT tag
    public String getNBT() {
        StringBuilder s = new StringBuilder("{");
        // Unbreakable
        if (unbreakable) {
            s.append("Unbreakable:1b");
        } else {
            s.append("Unbreakable:0b");
        }
        // Damage
        if (damage > 0) {
            s.append(",Damage:").append(damage);
        }
	// Display tags
	if (!name.getText().isEmpty() || lore.size() > 0) {
        s.append(",display:{");
        // Name
        if (!name.getText().isEmpty()) {
            s.append("Name:");
            s.append(name.asNBT());
            if (lore.size() > 0) {
                s.append(",");
            }
        }
        // Lore
        if (lore.size() > 0) {
            s.append("Lore:[");
            for (int i = 0; i < lore.size(); i++) {
                s.append(lore.get(i).asNBT());
                if (i != lore.size() - 1) {
                    s.append(",");
                }
            }
            s.append("]");
        }
        s.append("}");
    }
	// Flags
	if (flags != 0) {
        s.append(",HideFlags:").append(flags);
    }
	// Enchants
	if (enchants.size() > 0) {
        s.append(",Enchantments:[");
        for (Map.Entry<String, Integer> entry : enchants.entrySet()) {
            s.append("{id:\"minecraft:");
            s.append(entry.getKey().toLowerCase());
            s.append("\",lvl:").append(entry.getValue()).append("s},");
        }
        s = new StringBuilder(s.substring(0, s.length() - 1));
        s.append("]");
    }
	// Attributes
	if (attributes.size() > 0) {
        s.append(",AttributeModifiers:[");
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String[] mods = entry.getValue().split(",");
            s.append("{AttributeName:\"").append(entry.getKey()).append("\",");
            s.append("Name:\"").append(entry.getKey()).append("\",");
            s.append("Amount:").append(mods[2]).append(",");
            s.append("Operation:").append(mods[0]).append(",");
            s.append("UUIDLeast:").append(GlobalVars.rand.nextInt() % 1000000 + 1).append(",UUIDMost:").append(GlobalVars.rand.nextInt() % 1000000 + 1).append(",");
            s.append("Slot:\"").append(mods[1]).append("\"");
            s.append("},");
        }
        s = new StringBuilder(s.substring(0, s.length() - 1));
        s.append("]");
    }
        s.append("}");
        return s.toString();
    }

    // Get the item as NBT (e.g. for nesting in other NBT)
    public String asNBT() {
	String s = "{";
	s += "id:\"minecraft:" + type.toLowerCase() + "\"";
	s += ",Count:" + count + "b";
	s += ",tag:" + getNBT();
	s += "}";
	return s;
    }

    public String getType() {
	return type;
    }

    public ColorText getName() {
	return name;
    }

    public List<ColorText> getLore() {
	return lore;
    }

    public Map<String, Integer> getEnchants() {
	return enchants;
    }

    public Map<String, String> getAttributes() {
	return attributes;
    }

    public int getCount() {
	return count;
    }

    public int getFlags() {
	return flags;
    }

    public boolean getUnbreakable() {
	return unbreakable;
    }

    public int getDamage() {
	return damage;
    }

    public void setType(String newVal) {
	type = newVal;
    }

    public void setName(ColorText newVal) {
	name = newVal;
    }

    public void setLore(List<ColorText> newVal) {
	lore = newVal;
    }

    public void setEnchants(Map<String, Integer> newVal) {
	enchants = newVal;
    }

    public void setAttributes(Map<String, String> newVal) {
	attributes = newVal;
    }

    public void setCount(int newVal) {
	count = newVal;
    }

    public void setFlags(int newVal) {
	flags = newVal;
    }

    public void setUnbreakable(boolean newVal) {
	unbreakable = newVal;
    }

    public void setDamage(int newVal) {
	damage = newVal;
    }
}
