package me.samboycoding.uhcplugin.game

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.types.Colours
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashSet

data class Team(var name: String, var colour: Colours.ColourData) {

    val playerIDs: MutableSet<UUID> = HashSet()

    val players: List<Player>
    get() = playerIDs.mapNotNull { UHCPlugin.instance.server.getPlayer(it) }

    fun addPlayer(player: Player) {
        playerIDs.add(player.uniqueId)
        setNameColour(player)
    }

    fun removePlayer(player: Player) {
        playerIDs.remove(player.uniqueId)
        clearNameColour(player)
    }

    fun setNameColour(player: Player) {
        player.displayName = "${colour.chatColourCode}${player.name}${ChatColor.RESET}"
        player.playerListName = "${colour.chatColourCode}${player.name}${ChatColor.RESET}"
    }

    fun clearNameColour(player: Player) {
        player.displayName = "${colour.chatColourCode}${player.name}${ChatColor.RESET}"
        player.playerListName = "${colour.chatColourCode}${player.name}${ChatColor.RESET}"
    }
}