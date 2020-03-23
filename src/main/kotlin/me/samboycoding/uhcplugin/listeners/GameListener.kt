package me.samboycoding.uhcplugin.listeners

import me.samboycoding.uhcplugin.UHCPlugin
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class GameListener : Listener {

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        event.entity.health = event.entity.maxHealth
        event.entity.gameMode = GameMode.SPECTATOR
        event.entity.spectatorTarget = event.entity.killer

        @Suppress("DEPRECATION")
        event.entity.sendTitle("${ChatColor.RED}You died!", "${ChatColor.RED}Spectating")

        UHCPlugin.instance.server.scheduler.runTaskLater(UHCPlugin.instance, {
            event.entity.kickPlayer("You are out!")
        }, 200)
    }
}