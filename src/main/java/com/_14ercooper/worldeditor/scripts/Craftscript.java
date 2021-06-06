package com._14ercooper.worldeditor.scripts;

import org.bukkit.command.CommandSender;

import java.util.LinkedList;

public abstract class Craftscript {
    public abstract void perform(LinkedList<String> args, CommandSender player, String label);
}
