package com._14ercooper.worldeditor.player

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object PlayerManager {
    val playerWrapperMap = mutableMapOf<String, PlayerWrapper>()

    fun getPlayerWrapper(player: CommandSender): PlayerWrapper {
        return getPlayerWrapper(
            if (player is Player) {
                player.uniqueId.toString()
            } else {
                "console"
            }
        )
    }

    fun getPlayerWrapper(player: UUID): PlayerWrapper {
        return getPlayerWrapper(player.toString())
    }

    fun getPlayerWrapper(playerName: String): PlayerWrapper {
        return if (playerWrapperMap.containsKey(playerName)) {
            playerWrapperMap[playerName]!!
        } else {
            val playerWrapper = PlayerWrapper(playerName)
            playerWrapperMap[playerName] = playerWrapper
            playerWrapper
        }
    }

    fun deletePlayerWrapper(player : String) {
        if (playerWrapperMap.containsKey(player)) {
            playerWrapperMap.remove(player)
        }
    }
}