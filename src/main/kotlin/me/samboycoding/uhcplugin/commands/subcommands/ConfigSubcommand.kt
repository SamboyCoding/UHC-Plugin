package me.samboycoding.uhcplugin.commands.subcommands

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.commands.Subcommand
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class ConfigSubcommand : Subcommand() {

    override val description: String = "Change or set game options."
    override val usage: String = "config [option] [value]"


    override fun execute(sender: CommandSender?, args: List<String>?) {
        sender?.sendMessage("Not yet implemented.")
    }
}