// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.undo

import com._14ercooper.worldeditor.main.Main
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object UndoSystem {

    // What user undos do we currently have loaded?
    private var userUndos: MutableMap<String, UserUndo> = HashMap()

    // Find a user's undo using the name of their undo (either UUID or "console")
    @JvmStatic
    fun findUserUndo(name: String): UserUndo {
        return if (userUndos.containsKey(name)) {
            Main.logDebug("Found undo for user $name")
            userUndos[name]!!
        } else {
            Main.logDebug("Loading new undo for user $name")
            userUndos[name] = UserUndo(name)
            userUndos[name]!!
        }
    }

    // Find the undo, also performing the lookup for the name of the undo
    @JvmStatic
    fun findUserUndo(user: CommandSender?): UserUndo {
        return findUserUndo(
            if (user is Player) {
                user.uniqueId.toString()
            } else {
                "console"
            }
        )
    }

    // Flush the entire undo system to disk
    @JvmStatic
    fun flush(): Boolean {
        userUndos.forEach { (_, userUndo) -> userUndo.flush() }
        userUndos = HashMap()
        Main.logDebug("Flushed undo system")
        return true
    }

    @JvmStatic
    fun isFlushed(): Boolean {
        var userUndosEmpty = true
        for ((_, userUndo) in userUndos) {
            userUndosEmpty = userUndosEmpty && userUndo.undoElements.isEmpty()
        }
        return userUndos.isEmpty() && userUndosEmpty
    }
}