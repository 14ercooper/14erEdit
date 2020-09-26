package com._14ercooper.worldeditor.make;

import java.util.Map;

import org.bukkit.entity.Player;

public abstract class MakeGenerator {
    public abstract boolean make(Player p, Map<String, String> args);
}
