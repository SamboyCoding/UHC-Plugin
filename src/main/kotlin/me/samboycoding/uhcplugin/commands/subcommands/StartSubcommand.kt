package me.samboycoding.uhcplugin.commands.subcommands

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.commands.Subcommand
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StartSubcommand : Subcommand() {

    override val description: String = "Starts the game immediately."
    override val usage: String = "start [force]"

    override fun execute(sender: CommandSender?, args: List<String>?) {
        if (UHCPlugin.instance.game.isRunning) {
            sender?.sendMessage("${ChatColor.RED}Game is already in progress.")
        } else {
            if (args?.isNotEmpty()!! && args?.firstOrNull()?.equals("force", true)!!) {
                sender?.sendMessage("${ChatColor.GREEN}Force-starting the game!")
                UHCPlugin.instance.game.isRunning = true
            } else {
                val teamlessPlayers: List<Player> = UHCPlugin.instance.server.onlinePlayers.filter { UHCPlugin.instance.game.getPlayerTeam(it) == null }
                if (teamlessPlayers.isNotEmpty()) {
                    sender?.sendMessage("${ChatColor.RED}Refusing to start until the following players choose a team:")
                    teamlessPlayers.forEach {
                        sender?.sendMessage("${ChatColor.RED} - ${it.name}")
                    }
                    sender?.sendMessage("${ChatColor.RED}Use ${ChatColor.DARK_RED}start force ${ChatColor.RED}to start anyway.")
                } else {
                    sender?.sendMessage("${ChatColor.GREEN}Starting the game!")
                    UHCPlugin.instance.game.isRunning = true
                }
            }
        }
    }
}