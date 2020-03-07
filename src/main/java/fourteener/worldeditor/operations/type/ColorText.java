package fourteener.worldeditor.operations.type;

import java.io.Serializable;

public class ColorText implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String text;
	String color;
	String bold = "false";
	String italic = "false";
	String underlined = "false";
	String strikethrough = "false";
	String obfuscated = "false";
	
	public ColorText() {
		text = "";
		color = "white";
	}
	public ColorText(String txt) {
		text = txt;
		color = "white";
	}
	public ColorText(String txt, String col) {
		text = txt;
		color = col;
	}
	
	// Get as a Minecraft-formatted NBT text element
	public String asNBT() {
		String s = "'{";
		s += "\"text\":\"" + text.replace('_', ' ') + "\",";
		s += "\"color\":\"" + color + "\",";
		s += "\"bold\":\"" + bold + "\",";
		s += "\"italic\":\"" + italic + "\",";
		s += "\"underlined\":\"" + underlined + "\",";
		s += "\"strikethrough\":\"" + strikethrough + "\",";
		s += "\"obfuscated\":\"" + obfuscated + "\"";
		s += "}'";
		return s;
	}
	
	public String getText() {
		return text;
	}
	
	public void setBold (String newVal) {
		bold = newVal.toLowerCase();
	}
	public void setItalic (String newVal) {
		italic = newVal.toLowerCase();
	}
	public void setUnderlined (String newVal) {
		underlined = newVal.toLowerCase();
	}
	public void setStrikethrough (String newVal) {
		strikethrough = newVal.toLowerCase();
	}
	public void setObfuscated (String newVal) {
		obfuscated = newVal.toLowerCase();
	}
}
