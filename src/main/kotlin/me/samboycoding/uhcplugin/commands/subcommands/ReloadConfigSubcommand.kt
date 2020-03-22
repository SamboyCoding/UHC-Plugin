package me.samboycoding.uhcplugin.commands.subcommands

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.commands.Subcommand
import me.samboycoding.uhcplugin.events.ConfigReloadEvent
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class ReloadConfigSubcommand : Subcommand() {

    override val description: String = "Load game settings from the config file."
    override val usage: String = "reloadconfig"


    override fun execute(sender: CommandSender?, args: List<String>?) {
        if (UHCPlugin.instance.game.isRunning) {
            sender?.sendMessage("${ChatColor.RED}Game is already in progress.")
        } else {
            UHCPlugin.instance.reloadConfig()
            UHCPlugin.instance.server.pluginManager.callEvent(ConfigReloadEvent())
            sender?.sendMessage("${ChatColor.GREEN}Config reloaded.")
        }
    }
}