package me.samboycoding.uhcplugin

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.samboycoding.uhcplugin.command.CommandDisableAmnesty
import me.samboycoding.uhcplugin.command.CommandStart
import me.samboycoding.uhcplugin.model.PlayerData
import me.samboycoding.uhcplugin.model.Team
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class UHCPlugin : JavaPlugin() {

    var gson = GsonBuilder().setPrettyPrinting().create()

    //Config
    var disableFriendlyFire = true
    var maxPlayersPerTeam = 2

    //Gameplay vars
    var isAmnesty = false
    var hasGameStarted = false

    //Data
    var players = arrayListOf<PlayerData>()
    var teams: ArrayList<Team> = arrayListOf()

    //Files
    private lateinit var teamsDataFile: File
    private lateinit var playerDataFile: File

    override fun onEnable() {
        instance = this
        logger.info("Ready to do some UHC!")

        server.pluginManager.registerEvents(EventListener(), this)

        if (!dataFolder.exists())
            dataFolder.mkdirs()

        teamsDataFile = dataFolder.resolve("teams.json")

        if (teamsDataFile.exists())
            readTeamData()

        playerDataFile = dataFolder.resolve("players.json")

        if (playerDataFile.exists())
            readPlayerData()

        //Commands

        getCommand("start").executor = CommandStart()
        getCommand("disableamnesty").executor = CommandDisableAmnesty()

        //Set spawn to 0,0
        val world = server.getWorld("world")

        world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0), 0)
    }

    override fun onDisable() {
        logger.info("Disabled!")
    }

    private fun readTeamData() {
        logger.info("Loading teams data from file...")

        val listType = object : TypeToken<ArrayList<Team.SerializedTeam>>() {}.type
        val teamData = gson.fromJson<ArrayList<Team.SerializedTeam>>(teamsDataFile.readText(), listType)

        teams = teamData.map { it.deserialize() }.toMutableList() as ArrayList<Team>

        logger.info("Read ${teams.count()} teams")
    }

    fun writeTeamData() {
//        logger.info("Saving teams data...")

        val json = gson.toJson(teams.filter { it.playerIds.isNotEmpty() }.map { it.serialize() })

        teamsDataFile.writeText(json)

//        logger.info("Done")
    }

    private fun readPlayerData() {
        logger.info("Reading player data from file...")

        val listType = object : TypeToken<ArrayList<PlayerData>>() {}.type
        players = gson.fromJson(playerDataFile.readText(), listType)

        logger.info("Read data for ${players.count()} players.")
    }

    fun writePlayerData() {
//        logger.info("Saving player data...")

        val json = gson.toJson(players)

        playerDataFile.writeText(json)

//        logger.info("Done")
    }

    companion object {
        lateinit var instance: UHCPlugin
    }
}