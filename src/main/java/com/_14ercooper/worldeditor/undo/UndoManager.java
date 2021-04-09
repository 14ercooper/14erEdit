package com._14ercooper.worldeditor.undo;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UndoManager {
    private static final List<Undo> undoList = new ArrayList<>();

    public static Undo getUndo(Player owner) {
	for (Undo u : undoList) {
	    if (u.owner.equals(owner))
		return u;
	}
	Undo u = new Undo(owner);
	undoList.add(u);
	return u;
    }
}
