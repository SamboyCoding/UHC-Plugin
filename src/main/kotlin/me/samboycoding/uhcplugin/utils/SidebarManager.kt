package me.samboycoding.uhcplugin.utils

import me.samboycoding.uhcplugin.UHCPlugin
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import java.lang.StringBuilder

class SidebarManager {

    private fun displaySidebarText(player: Player, text: List<String>) {
        val scoreboard = UHCPlugin.instance.server.scoreboardManager.newScoreboard

        val objective = scoreboard?.registerNewObjective("sidebar", "dummy")

        text.forEachIndexed { index, line ->
            val stringBuilder = StringBuilder()
            stringBuilder.append(line)
            index.toString().forEach {
                stringBuilder.append(ChatColor.COLOR_CHAR).append(it)
            }
            val realLine = stringBuilder.toString()
            objective?.getScore(realLine)?.score = text.size - index
        }

        objective?.displayName = UHCPlugin.instance.config.getString("game.scoreboard_title")
                .replace('&', ChatColor.COLOR_CHAR)

        objective?.displaySlot = DisplaySlot.SIDEBAR

        player.scoreboard = scoreboard
    }

    fun displayLobbyScoreboard(player: Player) {
        val team = UHCPlugin.instance.game.getPlayerTeam(player)
        displaySidebarText(player, listOf(
                "${ChatColor.YELLOW}Your Name",
                "${ChatColor.WHITE}${player.name}",
                "",
                "${ChatColor.YELLOW}Your Team",
                (if (team != null) "${team.colour.chatColourCode}■ ${ChatColor.WHITE}${team.name}" else "${ChatColor.WHITE}Open your inventory"),
                (if (team != null) "${ChatColor.WHITE}${UHCPlugin.instance.game.getPlayerTeam(player)?.playerIDs?.size ?: 0} / ${UHCPlugin.instance.config.getInt("game.team_size")}" else "${ChatColor.WHITE}to choose a team"),
                "",
                "${ChatColor.YELLOW}Players",
                "${ChatColor.WHITE}${UHCPlugin.instance.server.onlinePlayers.size}",
                "",
                "${ChatColor.YELLOW}Teams",
                "${ChatColor.WHITE}${UHCPlugin.instance.game.teams.size}"
        )
        )
    }

    fun displayGameScoreboard(player: Player) {
        displaySidebarText(player, listOf(
            "${ChatColor.YELLOW}"
        )
        )
    }
}