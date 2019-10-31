package me.samboycoding.uhcplugin.command

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.runBackground
import me.samboycoding.uhcplugin.team
import net.minecraft.server.v1_8_R3.BlockEnderPortalFrame
import net.minecraft.server.v1_8_R3.BlockPosition
import net.minecraft.server.v1_8_R3.EnumDirection
import org.bukkit.*
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt
import kotlin.random.Random

class CommandStart : CommandExecutor {
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

        runBackground {
            val world = UHCPlugin.instance.server.getWorld("world")
            if (args.firstOrNull() != "portal") {
                if (args.firstOrNull() != "true") {
                    Bukkit.broadcastMessage("${ChatColor.GREEN}====Welcome to UHC!====")
                    Thread.sleep(1000)
                    Bukkit.broadcastMessage("In about a minute, the teams will be randomly spread out over a 10,000 block square centered here.")
                    Thread.sleep(2000)
                    Bukkit.broadcastMessage("You will then have two hours of PvP-free gameplay to gather resources in survival mode.")
                    Thread.sleep(2000)
                    Bukkit.broadcastMessage("This will come with a 1-hour and a 30-minute warning.")
                    Thread.sleep(2000)
                    Bukkit.broadcastMessage("After this point, PvP will be enabled, an end portal will be randomly generated, and the border will start to shrink.")
                    Thread.sleep(2000)
                    Bukkit.broadcastMessage("The position of the portal will be sent to all players. If you beat the dragon, you win!")
                    Thread.sleep(2000)
                    Bukkit.broadcastMessage("But be aware that everyone will know when you put an eye in the portal and when you go through.")
                    Thread.sleep(2000)
                    Bukkit.broadcastMessage("Alternatively, you could just kill everyone else.")
                    Thread.sleep(2000)
                    Bukkit.broadcastMessage("${ChatColor.GREEN}May the odds be ever in your- wait, wrong game. Good luck!")
                    Bukkit.broadcastMessage("${ChatColor.GOLD}Game starts in approximately 45 seconds.")
                    Thread.sleep(15_000)
                    Bukkit.broadcastMessage("${ChatColor.GOLD}Game starts in approximately 30 seconds.")
                    Thread.sleep(20_000)
                    Bukkit.broadcastMessage("${ChatColor.GOLD}Game starts in approximately 10 seconds.")
                    Thread.sleep(10_000)
                }

                Bukkit.broadcastMessage("${ChatColor.GOLD}Game starting!")

                Bukkit.getScheduler().callSyncMethod(UHCPlugin.instance) {

                    spreadPlayers(world)
                    UHCPlugin.instance.hasGameStarted = true
                    UHCPlugin.instance.isAmnesty = true

                    for (world in UHCPlugin.instance.server.worlds) {
                        world.worldBorder.center = Location(world, 0.0, 0.0, 0.0)
                        world.worldBorder.size = 10_000.0
                        world.worldBorder.warningDistance = 50
                        world.worldBorder.damageBuffer = 0.0
                        world.worldBorder.damageAmount = 10.0

                        world.setGameRuleValue("naturalRegeneration", "false")
                    }
                }.get()

                Bukkit.broadcastMessage("${ChatColor.RED}Done teleporting players. Grace period ends in 2 hours!")
                Thread.sleep(60 * 60 * 1000) //Lol
                Bukkit.broadcastMessage("${ChatColor.RED}Grace period ends in 1 hour!")


                Thread.sleep(30 * 60 * 1000)
                Bukkit.broadcastMessage("${ChatColor.RED}Grace period ends in 30 minutes!")

                Thread.sleep(29 * 60 * 1000)
                Bukkit.broadcastMessage("${ChatColor.RED}Grace period ends in 1 minute!")

                Thread.sleep(60_000)
                Bukkit.broadcastMessage("${ChatColor.RED}Grace period is over! FIGHT!")
                UHCPlugin.instance.isAmnesty = false
            }

            //Generate portal, y-level 30, within a 1000-block radius of centre
            val x1 = round(Random.nextDouble(-900.0, 900.0))
            val x2 = x1 + 9

            val z1 = round(Random.nextDouble(-900.0, 900.0))
            val z2 = z1 + 9

            val y1 = 30.0
            val y2 = 35.0

            Bukkit.getScheduler().callSyncMethod(UHCPlugin.instance) {
                //Make a dark oak cube
                fillBlocks(world, Material.WOOD, x1, y1, z1, x2, y2, z2)

                //Hollow it out
                fillBlocks(world, Material.AIR, x1 + 1, y1 + 1, z1 + 1, x2 - 1, y2 - 1, z2 - 1)

                //Fill the sides with iron bars
                fillBlocks(world, Material.IRON_FENCE, x1 + 1, y1 + 1, z1, x2 - 1, y2 - 1, z1)
                fillBlocks(world, Material.IRON_FENCE, x1 + 1, y1 + 1, z2, x2 - 1, y2 - 1, z2)
                fillBlocks(world, Material.IRON_FENCE, x1, y1 + 1, z1 + 1, x1, y2 - 1, z2 - 1)
                fillBlocks(world, Material.IRON_FENCE, x2, y1 + 1, z1 + 1, x2, y2 - 1, z2 - 1)

                //Portal
                fillBlocks(world, Material.ENDER_PORTAL_FRAME, x1 + 3, y1 + 1, z1 + 2, x1 + 5, y1 + 1, z1 + 2)
                fillBlocks(world, Material.ENDER_PORTAL_FRAME, x1 + 3, y1 + 1, z1 + 6, x1 + 5, y1 + 1, z1 + 6)
                fillBlocks(
                    world,
                    Material.ENDER_PORTAL_FRAME,
                    x1 + 2,
                    y1 + 1,
                    z1 + 3,
                    x1 + 2,
                    y1 + 1,
                    z1 + 5,
                    EnumDirection.WEST
                )
                fillBlocks(
                    world,
                    Material.ENDER_PORTAL_FRAME,
                    x1 + 6,
                    y1 + 1,
                    z1 + 3,
                    x1 + 6,
                    y1 + 1,
                    z1 + 5,
                    EnumDirection.WEST
                )

                Bukkit.broadcastMessage("${ChatColor.GOLD}The end portal is at ${x1 + 4}, 31, ${z1 + 4}")
            }.get()
        }

        return true
    }

    private fun fillBlocks(
        world: World,
        block: Material,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        facing: EnumDirection = EnumDirection.UP
    ) {
        for (x in x1.toInt()..x2.toInt())
            for (z in z1.toInt()..z2.toInt())
                for (y in y1.toInt()..y2.toInt()) {
                    world.getBlockAt(x, y, z).type = block

                    val chunk = (world.getBlockAt(x, y, z).chunk as CraftChunk).handle
                    val block = chunk.getTypeAbs(x, y, z)

                    if (facing != EnumDirection.UP) {
                        val data = (block as BlockEnderPortalFrame).blockData
                        data.set(BlockEnderPortalFrame.FACING, facing)
                        //TODO: Why exactly is this returning false?
                        chunk.world.setTypeUpdate(BlockPosition(x, y, z), data)
                    }
                }

    }

    private fun spreadPlayers(world: World) {
        val chosenLocations = arrayListOf<Location>()
        val minDistance = 1000

        for (team in UHCPlugin.instance.teams) {

            //Choose a location
            var x: Int
            var y: Int
            var z: Int

            do {
                x = Random.nextInt(-4900, 4900)
                z = Random.nextInt(-4900, 4900)

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

            block.type = Material.BEDROCK

            val location = Location(world, x.toDouble(), (y + 1).toDouble(), z.toDouble())

            team.players.forEach {
                it.teleport(location)
            }
            chosenLocations.add(location)

            UHCPlugin.instance.logger.info("${team.name} => $location")
        }

        for (player in UHCPlugin.instance.server.onlinePlayers) {
            if (player.team != null)
                player.gameMode = GameMode.SURVIVAL
            else
                player.gameMode = GameMode.SPECTATOR

            player.health = player.maxHealth
            player.foodLevel = 20
        }
    }

    private fun pythagoreanDistanceBetween(x1: Double, z1: Double, x2: Double, z2: Double): Double {
        return sqrt((x2 - x1).pow(2) + (z2 - z1).pow(2))
    }
}
