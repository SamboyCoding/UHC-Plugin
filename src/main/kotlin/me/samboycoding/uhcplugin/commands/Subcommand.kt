package me.samboycoding.uhcplugin.commands

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class Subcommand {

    abstract val description: String

    abstract fun execute(sender: CommandSender?, args: List<String>?)
}