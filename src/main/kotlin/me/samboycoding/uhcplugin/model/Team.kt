package me.samboycoding.uhcplugin.model

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

data class Team(
    val color: Colors.ColorData,
    val playerIds: ArrayList<UUID>,
    val name: String = "${color.colorCode}Team ${color.name}"
) {
    val players
        get() = playerIds.mapNotNull { Bukkit.getServer().getPlayer(it) }

    fun applyNamesToPlayer(player: Player) {
        val coloredName = "${color.colorCode}${player.name}${ChatColor.RESET}"
        player.displayName = coloredName
        player.playerListName = coloredName
    }

    fun addPlayer(player: Player) {
        if (playerIds.contains(player.uniqueId)) return

        applyNamesToPlayer(player)

        playerIds.add(player.uniqueId)

        Bukkit.broadcastMessage("${player.displayName} joined team ${color.colorCode}${color.name}")
    }

    fun removePlayer(player: Player) {
        playerIds.remove(player.uniqueId)
    }

    fun serialize(): SerializedTeam {
        return SerializedTeam(
            Colors.values.entries.find { it.value.name == color.name }?.key
                ?: error("Missing color data for ${color.name}"),
            playerIds,
            name
        )
    }

    data class SerializedTeam(var colorInt: Int, var playerIds: ArrayList<UUID>, var name: String) {
        fun deserialize(): Team {
            return Team(
                Colors.values[colorInt] ?: error("Missing color data for $colorInt"),
                playerIds,
                name
            )
        }
    }
}