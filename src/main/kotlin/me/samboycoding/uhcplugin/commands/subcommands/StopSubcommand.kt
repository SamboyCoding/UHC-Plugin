package me.samboycoding.uhcplugin.commands.subcommands

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.commands.Subcommand
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class StopSubcommand : Subcommand() {

    override val description: String = "Stops the game immediately."
    override val usage: String = "stop"

    override fun execute(sender: CommandSender?, args: List<String>?) {
        sender?.sendMessage("${ChatColor.RED}Stopping the game!")
        UHCPlugin.instance.game.isRunning = false
    }
}