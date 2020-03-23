package me.samboycoding.uhcplugin.commands.subcommands

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.commands.Subcommand
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class StartSubcommand : Subcommand() {

    override val description: String = "Starts the game immediately."
    override val usage: String = "start"

    override fun execute(sender: CommandSender?, args: List<String>?) {
        if (UHCPlugin.instance.game.isRunning) {
            sender?.sendMessage("${ChatColor.RED}Game is already in progress.")
        } else {
            sender?.sendMessage("${ChatColor.GREEN}Starting the game!")
            UHCPlugin.instance.game.isRunning = true
        }
    }
}