package com._14ercooper.worldeditor.scripts;

import java.util.LinkedList;

import org.bukkit.entity.Player;

public abstract class Craftscript {
	public abstract boolean perform (LinkedList<String> args, Player player, String label);
}
