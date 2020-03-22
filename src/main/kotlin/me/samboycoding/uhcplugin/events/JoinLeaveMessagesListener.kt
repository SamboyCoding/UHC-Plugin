package me.samboycoding.uhcplugin.events

import me.samboycoding.uhcplugin.UHCPlugin
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinLeaveMessagesListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.joinMessage = "${ChatColor.GRAY}[${ChatColor.GREEN}+${ChatColor.GRAY}] ${UHCPlugin.instance.game.getPlayerTeam(event.player)?.colour?.chatColourCode ?: ""}${event.player.name}"
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        event.quitMessage = "${ChatColor.GRAY}[${ChatColor.RED}-${ChatColor.GRAY}] ${UHCPlugin.instance.game.getPlayerTeam(event.player)?.colour?.chatColourCode ?: ""}${event.player.name}"
    }
}