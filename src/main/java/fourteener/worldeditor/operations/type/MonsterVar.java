package fourteener.worldeditor.operations.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fourteener.worldeditor.operations.Operator;

public class MonsterVar implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String type = "";
	ColorText name = new ColorText();
	Map<String, String> base = new HashMap<String, String>();
	Map<String, String> attributes = new HashMap<String,String>();
	List<String> passengers = new ArrayList<String>();
	List<String> tags = new ArrayList<String>();
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
		String s = "{";
		// Name
		s += "CustomName:" + name.asNBT() + "";
		// Tags
		for (Map.Entry<String, String> entry : base.entrySet()) {
			s += "," + entry.getKey() + ":";
			if (entry.getValue().equalsIgnoreCase("true")) {
				s += "1b";
			}
			else if (entry.getValue().equalsIgnoreCase("false")) {
				s += "0b";
			}
			else {
				s += entry.getValue();
			}
		}
		// Attributes
		if (attributes.size() > 0) {
			s += ",Attributes:[";
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				s += "{Name:generic." + entry.getKey();
				s += ",Base:" + entry.getValue();
				s += "},";
			}
			s = s.substring(0, s.length() - 1);
			s += "]";
		}
		// Gear and drop chances
		// Hands
		if (!(hand.isEmpty() && offhand.isEmpty())) {
			s += ",HandItems:[";
			if (hand.isEmpty()) s += "{}";
			else s += Operator.itemVars.get(hand).asNBT();
			if (offhand.isEmpty()) s += ",{}";
			else s += "," + Operator.itemVars.get(offhand).asNBT();
			s += "],HandDropChances:[";
			s += handDrop + "F," + offhandDrop + "F";
			s += "]";
		}
		// Armor
		if (!(head.isEmpty() && chest.isEmpty() && legs.isEmpty() && feet.isEmpty())) {
			s += ",ArmorItems:[";
			if (feet.isEmpty()) s += "{}";
			else s += "," + Operator.itemVars.get(feet).asNBT();
			if (legs.isEmpty()) s += ",{}";
			else s += "," + Operator.itemVars.get(legs).asNBT();
			if (chest.isEmpty()) s += ",{}";
			else s += "," + Operator.itemVars.get(chest).asNBT();
			if (head.isEmpty()) s += ",{}";
			else s += "," + Operator.itemVars.get(head).asNBT();
			s += "],ArmorDropChances:[";
			s +=  feetDrop + "F," + legsDrop + "F," + chestDrop + "F," + headDrop + "F";
			s += "]";
		}
		// Passengers
		if (passengers.size() > 0) {
			s += ",Passengers:[";
			for (String str : passengers) {
				s += Operator.monsterVars.get(str).asNBT() + ",";
			}
			s = s.substring(0,s.length() - 1);
			s += "]";
		}
		// Tags
		if (tags.size() > 0) {
			s += ",Tags:[";
			for (String str : tags) {
				s += "\"" + str + "\",";
			}
			s = s.substring(0,s.length() - 1);
			s += "]";
		}
		s += "}";
		return s;
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
