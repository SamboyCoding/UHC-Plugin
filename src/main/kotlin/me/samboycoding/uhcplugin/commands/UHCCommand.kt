package me.samboycoding.uhcplugin.commands

import me.samboycoding.uhcplugin.commands.subcommands.StartSubcommand
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class UHCCommand : CommandExecutor {

    private val subcommands = mapOf("start" to StartSubcommand())

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<String>): Boolean {
        if (args.isNotEmpty()) {
            subcommands[args.firstOrNull()]?.execute(sender, args.slice(1 until args.size))
        } else {
            sendUsage(sender)
        }
        return true
    }

    private fun sendUsage(recipient: CommandSender?) {
        recipient?.sendMessage("${ChatColor.DARK_GREEN}${ChatColor.BOLD}[Command List]")
        subcommands.forEach {
            recipient?.sendMessage("${ChatColor.GREEN} - uhc ${it.key} - ${it.value.description}")
        }
    }
}