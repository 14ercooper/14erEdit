package fourteener.worldeditor.worldeditor.macros.macros;

public class BasicTreeMacro extends Macro {
	
	public static Macro createMacro (String[] args) {
		return new Macro();
	}
	
	public boolean performMacro () {
		return false;
	}
}
