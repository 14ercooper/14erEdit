package com._14ercooper.worldeditor.undo

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object UndoSystem {

    // What user undos do we currently have loaded?
    var userUndos : MutableMap<String, UserUndo> = HashMap()

    // Find a user's undo using the name of their undo (either UUID or "console")
    @JvmStatic
    fun findUserUndo(name : String) : UserUndo {
        return if (userUndos.containsKey(name)) {
            userUndos[name]!!
        }
        else {
            userUndos.put(name, UserUndo())
            userUndos[name]!!
        }
    }

    // Find the undo, also performing the lookup for the name of the undo
    @JvmStatic
    fun findUserUndo(user : CommandSender?) : UserUndo {
        return findUserUndo(if (user is Player) {user.uniqueId.toString()} else {"console"})
    }

    // Flush the entire undo system to disk
    @JvmStatic
    fun flush() : Boolean {
        userUndos.forEach { (s, userUndo) -> userUndo.flush() }
        userUndos = HashMap()
        return true
    }
}