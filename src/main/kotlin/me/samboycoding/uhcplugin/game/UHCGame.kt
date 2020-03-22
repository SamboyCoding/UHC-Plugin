package me.samboycoding.uhcplugin.game

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.events.GameStartEvent
import me.samboycoding.uhcplugin.events.GameStopEvent
import me.samboycoding.uhcplugin.utils.Colours
import org.bukkit.entity.Player

class UHCGame {

    var isRunning: Boolean = false
        set(running) {
            if (running) {
                UHCPlugin.instance.server.pluginManager.callEvent(GameStartEvent())
            } else {
                UHCPlugin.instance.server.pluginManager.callEvent(GameStopEvent())
            }

            field = running
        }


    val teams: HashSet<Team> = HashSet()

    val playerCount: Int
        get() {
            return teams.fold(0, { acc, team ->
                acc + team.playerIDs.size
            })
        }

    fun initialiseDefaultTeams(count: Int) {
        Colours.values.take(count).forEach {
            teams.add(Team(it.name, it))
        }
    }

    fun getPlayerTeam(player: Player): Team? {
        return teams.find { it.playerIDs.contains(player.uniqueId) }
    }
}