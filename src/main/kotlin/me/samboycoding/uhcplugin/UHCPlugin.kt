package me.samboycoding.uhcplugin

import me.samboycoding.uhcplugin.commands.UHCCommand
import me.samboycoding.uhcplugin.events.JoinLeaveMessagesListener
import me.samboycoding.uhcplugin.game.RecipeManager
import me.samboycoding.uhcplugin.game.UHCGame
import me.samboycoding.uhcplugin.listeners.*
import me.samboycoding.uhcplugin.utils.SidebarManager
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class UHCPlugin : JavaPlugin() {

    lateinit var game: UHCGame
    lateinit var sidebar: SidebarManager
    lateinit var recipe: RecipeManager

    override fun onEnable() {
        // maintain a static instance of the plugin for the sake of convenience.
        instance = this

        saveDefaultConfig()

        game = UHCGame()
        sidebar = SidebarManager()
        recipe = RecipeManager()

        game.initialiseDefaultTeams(config.getInt("game.teams"))

        getCommand("uhc").executor = UHCCommand()

        server.pluginManager.registerEvents(SetupListener(), this)
        server.pluginManager.registerEvents(PortalListener(), this)
        server.pluginManager.registerEvents(LobbyListener(), this)
        server.pluginManager.registerEvents(ScoreboardListener(), this)
        server.pluginManager.registerEvents(JoinLeaveMessagesListener(), this)
        server.pluginManager.registerEvents(CraftingListener(), this)
        server.pluginManager.registerEvents(GameListener(), this)

        recipe.addGoldenHeadRecipe()
    }

    companion object {
        lateinit var instance: UHCPlugin
    }
}