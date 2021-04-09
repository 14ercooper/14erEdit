package com._14ercooper.worldeditor.operations.type;

import com._14ercooper.worldeditor.operations.Operator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonsterVar implements Serializable {
    private static final long serialVersionUID = 1L;

    String type = "";
    ColorText name = new ColorText();
    final Map<String, String> base = new HashMap<>();
    final Map<String, String> attributes = new HashMap<>();
    final Map<String, String> effects = new HashMap<>();
    final List<String> passengers = new ArrayList<>();
    final List<String> tags = new ArrayList<>();
    String hand = "";
    String offhand = "";
    String head = "";
    String chest = "";
    String legs = "";
    String feet = "";
    String handDrop = "0.085";
    String offhandDrop = "0.085";
    String headDrop = "0.085";
    String chestDrop = "0.085";
    String legsDrop = "0.085";
    String feetDrop = "0.085";

    public String getNBT() {
        if (name.getText().isEmpty()) {
            name = new ColorText(type);
        }
        StringBuilder s = new StringBuilder("{");
        // Name
        s.append("CustomName:").append(name.asNBT());
        // Tags
        for (Map.Entry<String, String> entry : base.entrySet()) {
            s.append(",").append(entry.getKey()).append(":");
            if (entry.getValue().equalsIgnoreCase("true")) {
                s.append("1b");
            } else if (entry.getValue().equalsIgnoreCase("false")) {
                s.append("0b");
            } else {
                s.append(entry.getValue());
            }
        }
	// Attributes
	if (attributes.size() > 0) {
        s.append(",Attributes:[");
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            s.append("{Name:generic.").append(entry.getKey());
            s.append(",Base:").append(entry.getValue());
            s.append("},");
        }
        s = new StringBuilder(s.substring(0, s.length() - 1));
        s.append("]");
    }
	// Effects
	if (effects.size() > 0) {
        s.append(",ActiveEffects:[");
        for (Map.Entry<String, String> entry : effects.entrySet()) {
            s.append("{Id:").append(entry.getKey());
            s.append("b,:").append(entry.getValue());
            s.append("},");
        }
        s = new StringBuilder(s.substring(0, s.length() - 1));
        s.append("]");
    }
	// Gear and drop chances
	// Hands
	if (!(hand.isEmpty() && offhand.isEmpty())) {
        s.append(",HandItems:[");
        if (hand.isEmpty())
            s.append("{}");
        else
            s.append(Operator.itemVars.get(hand).asNBT());
        if (offhand.isEmpty())
            s.append(",{}");
        else
            s.append(",").append(Operator.itemVars.get(offhand).asNBT());
        s.append("],HandDropChances:[");
        s.append(handDrop).append("F,").append(offhandDrop).append("F");
        s.append("]");
    }
	// Armor
	if (!(head.isEmpty() && chest.isEmpty() && legs.isEmpty() && feet.isEmpty())) {
        s.append(",ArmorItems:[");
        if (feet.isEmpty())
            s.append("{}");
        else
            s.append(",").append(Operator.itemVars.get(feet).asNBT());
        if (legs.isEmpty())
            s.append(",{}");
        else
            s.append(",").append(Operator.itemVars.get(legs).asNBT());
        if (chest.isEmpty())
            s.append(",{}");
        else
            s.append(",").append(Operator.itemVars.get(chest).asNBT());
        if (head.isEmpty())
            s.append(",{}");
        else
            s.append(",").append(Operator.itemVars.get(head).asNBT());
        s.append("],ArmorDropChances:[");
        s.append(feetDrop).append("F,").append(legsDrop).append("F,").append(chestDrop).append("F,").append(headDrop).append("F");
        s.append("]");
    }
	// Passengers
	if (passengers.size() > 0) {
        s.append(",Passengers:[");
        for (String str : passengers) {
            s.append(Operator.monsterVars.get(str).asNBT()).append(",");
        }
        s = new StringBuilder(s.substring(0, s.length() - 1));
        s.append("]");
    }
        // Tags
        if (tags.size() > 0) {
            s.append(",Tags:[");
            for (String str : tags) {
                s.append("\"").append(str).append("\",");
            }
            s = new StringBuilder(s.substring(0, s.length() - 1));
            s.append("]");
        }
        s.append("}");
        return s.toString();
    }

    public String asNBT() {
	String s = "{id:\"minecraft:" + type.toLowerCase() + "\",";
	String str = getNBT().substring(1);
	s += str;
	return s;
    }

    public String getType() {
	return type;
    }

    public void setType(String newType) {
	type = newType;
    }

    public void setName(ColorText newName) {
	name = newName;
    }

    public void setBase(String name, String value) {
	base.put(name, value);
    }

    public void setAttribute(String name, String value) {
	attributes.put(name, value);
    }

    public void setEffect(String id, String level, String duration) {
	String value = "Amplifier:" + level + "b,Duration:" + duration;
	attributes.put(id, value);
    }

    public void setGear(String slot, String name) {
	if (slot.equalsIgnoreCase("hand")) {
	    hand = name;
	}
	if (slot.equalsIgnoreCase("offhand")) {
	    offhand = name;
	}
	if (slot.equalsIgnoreCase("head") || slot.equalsIgnoreCase("helmet")) {
	    head = name;
	}
	if (slot.equalsIgnoreCase("chest") || slot.equalsIgnoreCase("body")) {
	    chest = name;
	}
	if (slot.equalsIgnoreCase("legs") || slot.equalsIgnoreCase("pants")) {
	    legs = name;
	}
	if (slot.equalsIgnoreCase("feet") || slot.equalsIgnoreCase("boots")) {
	    feet = name;
	}
    }

    public void setDrop(String slot, String val) {
	if (slot.equalsIgnoreCase("hand")) {
	    handDrop = val;
	}
	if (slot.equalsIgnoreCase("offhand")) {
	    offhandDrop = val;
	}
	if (slot.equalsIgnoreCase("head") || slot.equalsIgnoreCase("helmet")) {
	    headDrop = val;
	}
	if (slot.equalsIgnoreCase("chest") || slot.equalsIgnoreCase("body")) {
	    chestDrop = val;
	}
	if (slot.equalsIgnoreCase("legs") || slot.equalsIgnoreCase("pants")) {
	    legsDrop = val;
	}
	if (slot.equalsIgnoreCase("feet") || slot.equalsIgnoreCase("boots")) {
	    feetDrop = val;
	}
    }

    public void addPassenger(String mob) {
	passengers.add(mob);
    }

    public void addTag(String tag) {
	tags.add(tag);
    }
}
