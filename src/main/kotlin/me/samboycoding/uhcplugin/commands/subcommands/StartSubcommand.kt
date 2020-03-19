package me.samboycoding.uhcplugin.commands.subcommands

import me.samboycoding.uhcplugin.commands.Subcommand
import org.bukkit.command.CommandSender

class StartSubcommand : Subcommand() {

    override val description: String = "Starts the game"

    override fun execute(sender: CommandSender?, args: List<String>?) {
        sender?.sendMessage("The start subcommand was called. Args: ${args?.joinToString()}")
    }
}