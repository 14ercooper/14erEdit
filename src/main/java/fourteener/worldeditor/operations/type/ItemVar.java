package fourteener.worldeditor.operations.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemVar {
	String type = "";
	String name = "";
	List<String> lore = new ArrayList<String>();
	Map<String, Integer> enchants = new HashMap<String, Integer>();
	Map<String, String> attributes = new HashMap<String, String>();
	int count = 1;
	List<String> flags = new ArrayList<String>();
	
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public List<String> getLore() {
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
	public List<String> getFlags() {
		return flags;
	}
	
	public void setType(String newVal) {
		type = newVal;
	}
	public void setName(String newVal) {
		name = newVal;
	}
	public void setLore(List<String> newVal) {
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
	public void setFlags(List<String> newVal) {
		flags = newVal;
	}
}
