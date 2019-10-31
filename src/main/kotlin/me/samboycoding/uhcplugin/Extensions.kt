package me.samboycoding.uhcplugin

import org.bukkit.entity.Player

val Player.team
    get() = UHCPlugin.instance.teams.find { it.playerIds.contains(uniqueId) }

fun Any.runBackground(runnable: () -> Unit) {
    Thread {
        runnable()
    }.apply {
        name = "Background Thread"
        isDaemon = true
        start()
    }
}