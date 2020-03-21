package me.samboycoding.uhcplugin

import me.samboycoding.uhcplugin.commands.UHCCommand
import me.samboycoding.uhcplugin.game.UHCGame
import me.samboycoding.uhcplugin.listeners.PortalListener
import me.samboycoding.uhcplugin.listeners.SetupListener
import org.bukkit.plugin.java.JavaPlugin

class UHCPlugin : JavaPlugin() {

    lateinit var game: UHCGame

    override fun onEnable() {
        // maintain a static instance of the plugin for the sake of convenience.
        instance = this

        saveDefaultConfig()

        game = UHCGame()

        getCommand("uhc").executor = UHCCommand()

        server.pluginManager.registerEvents(SetupListener(), this)
        server.pluginManager.registerEvents(PortalListener(), this)
    }

    companion object {
        lateinit var instance: UHCPlugin
    }
}