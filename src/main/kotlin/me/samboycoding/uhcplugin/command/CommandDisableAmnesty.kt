package me.samboycoding.uhcplugin.command

import me.samboycoding.uhcplugin.UHCPlugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CommandDisableAmnesty : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("${ChatColor.RED}You can't do that!")
            return true
        }

        Bukkit.broadcastMessage("${ChatColor.RED}Grace period is over! FIGHT!")
        UHCPlugin.instance.isAmnesty = false
        UHCPlugin.instance.hasGameStarted = true

        return true
    }

}