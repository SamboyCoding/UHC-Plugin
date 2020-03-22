package me.samboycoding.uhcplugin.listeners

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.events.ConfigReloadEvent
import me.samboycoding.uhcplugin.events.PlayerTeamSwitchEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ScoreboardListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) {
        displayAllScoreboards()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onLeave(event: PlayerQuitEvent) {
        displayAllScoreboards()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onTeamSwitch(event: PlayerTeamSwitchEvent) {
        displayAllScoreboards()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onConfigReload(event: ConfigReloadEvent) {
        displayAllScoreboards()
    }

    private fun displayAllScoreboards() {
        if (!UHCPlugin.instance.game.isRunning) {
            UHCPlugin.instance.server.onlinePlayers.forEach {
                UHCPlugin.instance.sidebar.displayLobbyScoreboard(it)
            }
        } else {
            UHCPlugin.instance.server.onlinePlayers.forEach {
                UHCPlugin.instance.sidebar.displayGameScoreboard(it)
            }
        }
    }

    private fun displayPlayerScoreboard(player: Player) {
        if (!UHCPlugin.instance.game.isRunning) {
                UHCPlugin.instance.sidebar.displayLobbyScoreboard(player)
        } else {
                UHCPlugin.instance.sidebar.displayGameScoreboard(player)
        }
    }
}