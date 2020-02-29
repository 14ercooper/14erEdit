package fourteener.worldeditor.operations.type;

public class BlockVar {
	String type;
	String data;
	String nbt;
	
	public String getType() {
		return type;
	}
	public String getData() {
		return data;
	}
	public String getNBT() {
		return nbt;
	}
	
	public void setType(String newVal) {
		type = newVal;
	}
	public void setData(String newVal) {
		data = newVal;
	}
	public void setNbt(String newVal) {
		nbt = newVal;
	}
}
