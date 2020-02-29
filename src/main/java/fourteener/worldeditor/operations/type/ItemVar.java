package fourteener.worldeditor.operations.type;

import java.util.List;
import java.util.Map;

public class ItemVar {
	String type;
	String name;
	List<String> lore;
	Map<String, Integer> enchants;
	Map<String, String> attributes;
	int count;
	List<String> flags;
	
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
	public void setFlag(List<String> newVal) {
		flags = newVal;
	}
}
