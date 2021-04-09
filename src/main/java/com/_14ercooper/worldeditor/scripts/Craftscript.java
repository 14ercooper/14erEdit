package com._14ercooper.worldeditor.scripts;

import org.bukkit.entity.Player;

import java.util.LinkedList;

public abstract class Craftscript {
    public abstract void perform(LinkedList<String> args, Player player, String label);
}
