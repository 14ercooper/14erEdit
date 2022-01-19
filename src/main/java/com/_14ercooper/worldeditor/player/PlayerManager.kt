// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.player

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object PlayerManager {
    val playerWrapperMap = mutableMapOf<String, PlayerWrapper>()

    @JvmStatic
    fun getPlayerWrapper(player: CommandSender): PlayerWrapper {
        return getPlayerWrapper(
            if (player is Player) {
                player.uniqueId.toString()
            } else {
                "console"
            }
        )
    }

    @JvmStatic
    fun getPlayerWrapper(player: UUID): PlayerWrapper {
        return getPlayerWrapper(player.toString())
    }

    @JvmStatic
    fun getPlayerWrapper(playerName: String): PlayerWrapper {
        return if (playerWrapperMap.containsKey(playerName)) {
            playerWrapperMap[playerName]!!
        } else {
            val playerWrapper = PlayerWrapper(playerName)
            playerWrapperMap[playerName] = playerWrapper
            playerWrapper
        }
    }

    @JvmStatic
    fun deletePlayerWrapper(player: String) {
        if (playerWrapperMap.containsKey(player)) {
            playerWrapperMap.remove(player)
        }
    }
}