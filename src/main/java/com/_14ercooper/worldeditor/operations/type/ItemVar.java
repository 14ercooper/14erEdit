package com._14ercooper.worldeditor.operations.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com._14ercooper.worldeditor.main.GlobalVars;

public class ItemVar implements Serializable {
    private static final long serialVersionUID = 1L;

    String type = "";
    ColorText name = new ColorText();
    List<ColorText> lore = new ArrayList<ColorText>();
    Map<String, Integer> enchants = new HashMap<String, Integer>();
    Map<String, String> attributes = new HashMap<String, String>();
    int count = 1;
    int flags = 0;
    int damage = 0;
    boolean unbreakable = false;

    // Get the item's NBT tag
    public String getNBT() {
	String s = "{";
	// Unbreakable
	if (unbreakable) {
	    s += "Unbreakable:1b";
	}
	else {
	    s += "Unbreakable:0b";
	}
	// Damage
	if (damage > 0) {
	    s += ",Damage:" + damage;
	}
	// Display tags
	if (!name.getText().isEmpty() || lore.size() > 0) {
	    s += ",display:{";
	    // Name
	    if (!name.getText().isEmpty()) {
		s += "Name:";
		s += name.asNBT();
		if (lore.size() > 0) {
		    s += ",";
		}
	    }
	    // Lore
	    if (lore.size() > 0) {
		s += "Lore:[";
		for (int i = 0; i < lore.size(); i++) {
		    s += lore.get(i).asNBT();
		    if (i != lore.size() - 1) {
			s += ",";
		    }
		}
		s += "]";
	    }
	    s += "}";
	}
	// Flags
	if (flags != 0) {
	    s += ",HideFlags:" + flags;
	}
	// Enchants
	if (enchants.size() > 0) {
	    s += ",Enchantments:[";
	    for (Map.Entry<String, Integer> entry : enchants.entrySet()) {
		s += "{id:\"minecraft:";
		s += entry.getKey().toLowerCase();
		s += "\",lvl:" + entry.getValue() + "s},";
	    }
	    s = s.substring(0, s.length() - 1);
	    s += "]";
	}
	// Attributes
	if (attributes.size() > 0) {
	    s += ",AttributeModifiers:[";
	    for (Map.Entry<String, String> entry : attributes.entrySet()) {
		String[] mods = entry.getValue().split(",");
		s += "{AttributeName:\"" + entry.getKey() + "\",";
		s += "Name:\"" + entry.getKey() + "\",";
		s += "Amount:" + mods[2] + ",";
		s += "Operation:" + mods[0] + ",";
		s += "UUIDLeast:" + (GlobalVars.rand.nextInt() % 1000000 + 1) + ",UUIDMost:" + (GlobalVars.rand.nextInt() % 1000000 + 1)
			+ ",";
		s += "Slot:\"" + mods[1] + "\"";
		s += "},";
	    }
	    s = s.substring(0, s.length() - 1);
	    s += "]";
	}
	s += "}";
	return s;
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
