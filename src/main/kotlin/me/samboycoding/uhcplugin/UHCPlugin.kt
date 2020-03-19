package me.samboycoding.uhcplugin

import me.samboycoding.uhcplugin.commands.UHCCommand
import org.bukkit.plugin.java.JavaPlugin

class UHCPlugin : JavaPlugin() {

    override fun onEnable() {
        // maintain a static instance of the plugin for the sake of convenience
        instance = this

        getCommand("uhc").executor = UHCCommand()
    }

    companion object {
        lateinit var instance: UHCPlugin
    }
}