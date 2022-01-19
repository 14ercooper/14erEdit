// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.scripts;

import org.bukkit.command.CommandSender;

import java.util.LinkedList;

public abstract class Craftscript {
    public abstract void perform(LinkedList<String> args, CommandSender player, String label);
}
