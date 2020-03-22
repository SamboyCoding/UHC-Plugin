package me.samboycoding.uhcplugin.commands

import me.samboycoding.uhcplugin.commands.subcommands.ReloadConfigSubcommand
import me.samboycoding.uhcplugin.commands.subcommands.StartSubcommand
import me.samboycoding.uhcplugin.commands.subcommands.StopSubcommand
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class UHCCommand : CommandExecutor {

    private val subcommands = mapOf(
            "start" to StartSubcommand(),
            "stop" to StopSubcommand(),
            "reloadconfig" to ReloadConfigSubcommand()
    )

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<String>): Boolean {
        if (args.isNotEmpty() && subcommands.containsKey(args.firstOrNull())) {
            subcommands[args.firstOrNull()]?.execute(sender, args.slice(1 until args.size))
        } else {
            sendUsage(sender, label)
        }
        return true
    }

    private fun sendUsage(recipient: CommandSender?, label: String?) {
        recipient?.sendMessage("${ChatColor.DARK_GREEN}${ChatColor.BOLD}[Command List]")
        subcommands.forEach {
            recipient?.sendMessage("${ChatColor.DARK_GREEN}${label} ${it.value.usage} ${ChatColor.GREEN}${it.value.description}")
        }
    }
}