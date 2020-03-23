package me.samboycoding.uhcplugin.listeners

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.events.GameStartEvent
import me.samboycoding.uhcplugin.events.GameStopEvent
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.io.File
import java.util.*
import java.util.logging.Logger
import kotlin.math.pow
import kotlin.math.sqrt


class SetupListener : Listener {

    @EventHandler
    fun onStart(event: GameStartEvent) {
        UHCPlugin.instance.server.onlinePlayers.forEach {
            @Suppress("DEPRECATION")
            it.sendTitle("${ChatColor.GREEN}Get ready!", "${ChatColor.GREEN}UHC is starting now!")
        }
        UHCPlugin.instance.server.broadcastMessage("${ChatColor.YELLOW}Please wait while the world is generated.")

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

        world.worldBorder.setCenter(0.0, 0.0)
        world.worldBorder.size = UHCPlugin.instance.config.getDouble("game.border_size")

        log.info("Moving players to game world")
        UHCPlugin.instance.server.onlinePlayers.forEach { it.teleport(world.spawnLocation) }

        log.info("Spreading uwu")
        spreadPlayers(world)

        log.info("Healing players")
        UHCPlugin.instance.server.onlinePlayers.forEach {
            if (UHCPlugin.instance.game.getPlayerTeam(it) != null)
                it.gameMode = GameMode.SURVIVAL
            else
                it.gameMode = GameMode.SPECTATOR

            it.health = it.maxHealth
            it.foodLevel = 20
        }

        UHCPlugin.instance.server.broadcastMessage("${ChatColor.YELLOW}The grace period begins now. Good luck!")
    }

    @EventHandler
    fun onStop(event: GameStopEvent) {
        UHCPlugin.instance.server.broadcastMessage("${ChatColor.YELLOW}UHC is over!")

        val log: Logger = UHCPlugin.instance.logger

        log.info("Removing players from game worlds")
        val tpLocation: Location = UHCPlugin.instance.server.worlds[0].spawnLocation
        UHCPlugin.instance.server.onlinePlayers.forEach {
            it.teleport(tpLocation)
            it.gameMode = GameMode.ADVENTURE
        }
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

    private fun spreadPlayers(world: World) {
        val chosenLocations = arrayListOf<Location>()
        val minDistance = 1000

        UHCPlugin.instance.game.teams.forEach { team ->
            if (team.players.isEmpty()) { // skip empty teams
                return
            }

            //Choose a location
            var x: Int
            var y: Int
            var z: Int

            do {
                x = kotlin.random.Random.nextInt(-4900, 4900)
                z = kotlin.random.Random.nextInt(-4900, 4900)

                y = world.getHighestBlockYAt(x, z)
            } while (chosenLocations.any {
                        pythagoreanDistanceBetween(
                                x.toDouble(),
                                it.x,
                                z.toDouble(),
                                it.z
                        ) < minDistance
                    })

            //Got a location
            val block = world.getBlockAt(x, y, z)

            block.type = Material.OBSIDIAN

            val location = Location(world, x.toDouble(), (y + 1).toDouble(), z.toDouble())

            team.players.forEach {
                it.teleport(location)
            }
            chosenLocations.add(location)

            UHCPlugin.instance.logger.info("${team.name} => $location")
        }
    }

    private fun pythagoreanDistanceBetween(x1: Double, z1: Double, x2: Double, z2: Double): Double {
        return sqrt((x2 - x1).pow(2) + (z2 - z1).pow(2))
    }
}