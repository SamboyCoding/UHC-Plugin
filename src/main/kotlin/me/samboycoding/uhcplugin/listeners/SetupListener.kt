package me.samboycoding.uhcplugin.listeners

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.events.GameStartEvent
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.io.File
import java.util.*
import java.util.logging.Logger


class SetupListener : Listener {

    @EventHandler
    fun onStart(event: GameStartEvent) {
        UHCPlugin.instance.server.broadcastMessage("${ChatColor.YELLOW}UHC is starting now - please wait a moment!")

        val log: Logger = UHCPlugin.instance.logger

        val gameWorldName: String = UHCPlugin.instance.config.getString("game.game_world")
        val gameSeed: Long = UHCPlugin.instance.config.getLong("game.seed")

        log.info("Removing players from game worlds")
        val tpLocation: Location = UHCPlugin.instance.server.worlds[0].spawnLocation
        UHCPlugin.instance.server.onlinePlayers.forEach { it.teleport(tpLocation) }

        log.info("Unloading game worlds")
        UHCPlugin.instance.server.unloadWorld(gameWorldName, false)
        UHCPlugin.instance.server.unloadWorld("${gameWorldName}_nether", false)
        UHCPlugin.instance.server.unloadWorld("${gameWorldName}_the_end", false)

        log.info("Deleting game worlds")
        File(UHCPlugin.instance.server.worldContainer, gameWorldName).deleteRecursively()
        File(UHCPlugin.instance.server.worldContainer, "${gameWorldName}_nether").deleteRecursively()
        File(UHCPlugin.instance.server.worldContainer, "${gameWorldName}_the_end").deleteRecursively()

        log.info("Creating game worlds (${gameWorldName})")
        val world = createWorld(gameWorldName, World.Environment.NORMAL, gameSeed)
        createWorld("${gameWorldName}_nether", World.Environment.NETHER, gameSeed)
        createWorld("${gameWorldName}_the_end", World.Environment.THE_END, gameSeed)

        val highestSpawnBlock: Location = world.getHighestBlockAt(0, 0).location
        world.setSpawnLocation(highestSpawnBlock.blockX, highestSpawnBlock.blockY, highestSpawnBlock.blockZ)

        log.info("Moving players to game world")
        UHCPlugin.instance.server.onlinePlayers.forEach { it.teleport(world.spawnLocation) }
    }

    private fun createWorld(name: String, env: World.Environment, seed: Long): World {
        val worldCreator = WorldCreator(name)

        if (seed == 0L) {
            worldCreator.seed(Random().nextLong())
        } else {
            worldCreator.seed(seed)
        }

        worldCreator.environment(env)

        return UHCPlugin.instance.server.createWorld(worldCreator)
    }
}