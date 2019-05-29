package fourteener.worldeditor.worldeditor.undo;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class UndoManager {
	private static List<Undo> undoList = new ArrayList<Undo>();
	
	public static Undo getUndo (Player owner) {
		for (Undo u : undoList) {
			if (u.owner.equals(owner))
				return u;
		}
		Undo u = Undo.newUndo(owner);
		undoList.add(u);
		return u;
	}
}
