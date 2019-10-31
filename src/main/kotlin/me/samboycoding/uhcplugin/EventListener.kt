package me.samboycoding.uhcplugin

import me.samboycoding.uhcplugin.model.Colors
import me.samboycoding.uhcplugin.model.PlayerData
import me.samboycoding.uhcplugin.model.Team
import org.bukkit.*
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemStack

class EventListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val existingData = UHCPlugin.instance.players.find { it.uuid == event.player.uniqueId }

        if (existingData == null) {
            UHCPlugin.instance.logger.info("Creating data for player ${event.player.name} / ${event.player.uniqueId}")

            UHCPlugin.instance.players.add(PlayerData(event.player.uniqueId))

            UHCPlugin.instance.writePlayerData()

            event.player.inventory.addItem(ItemStack(Material.COMPASS, 1))
            event.player.sendMessage("${ChatColor.GREEN}Welcome - looks like you're new here. You can use the compass to select a team.")
            event.player.gameMode = if (UHCPlugin.instance.hasGameStarted) GameMode.SPECTATOR else GameMode.ADVENTURE
        } else {
            event.player.team?.applyNamesToPlayer(event.player)
        }

        event.joinMessage = "${ChatColor.YELLOW}${event.player.displayName
            ?: event.player.name}${ChatColor.YELLOW} is going ultra hardcore!"
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        event.player.inventory.addItem(ItemStack(Material.COMPASS, 1))
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        event.quitMessage =
            "${ChatColor.YELLOW}${event.player.displayName ?: event.player.name}${ChatColor.YELLOW} left."
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        if (!UHCPlugin.instance.hasGameStarted)
            event.isCancelled = true
    }

    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {
        if (event.item == null) return

        if (event.item.type == Material.COMPASS && !UHCPlugin.instance.hasGameStarted) {
            //Create a GUI - two rows
            val teamsGui = GenericGui(18, "Choose Team")

            for (i in 0 until 16) {
                if (!Colors.values.containsKey(i)) continue

                //Do we already have a team defined for this color? If so, we want to use its name in the list
                val team = UHCPlugin.instance.teams.find { it.color == Colors.values[i] }

                val lore = arrayListOf<String>()

                if (team != null && team.players.isNotEmpty()) {
                    val pluralChar = if (team.players.count() == 1) "" else "s"

                    lore.add("${ChatColor.GREEN}There's ${team.players.count()} player$pluralChar on this team:")
                    for (player in team.players)
                        lore.add("${ChatColor.GREEN}    -${player.name}")

                    if (team.players.count() >= UHCPlugin.instance.maxPlayersPerTeam)
                        lore.add("${ChatColor.RED}This team is full!")
                }

                //Wool with whatever color and the team name (or "Team xxx" by default)
                teamsGui.addItem(
                    Material.WOOL,
                    { stack, player ->
                        //Get the color
                        val colorInt = stack.durability.toInt()
                        val color = Colors.values[colorInt] ?: error("Missing color data for color $colorInt")

                        //Find if we have a team. Don't allow this to be modified by another player in the mean time
                        synchronized(UHCPlugin.instance.teams) {
                            var teamToJoin = UHCPlugin.instance.teams.find { it.color == color }

                            if (teamToJoin != null && teamToJoin.players.count() >= UHCPlugin.instance.maxPlayersPerTeam) {
                                player.sendMessage("${ChatColor.RED}That team is full!")

                                //Don't close GUI
                                return@addItem false
                            }

                            //If not, make one
                            if (teamToJoin == null) {
                                teamToJoin = Team(color, arrayListOf())
                                UHCPlugin.instance.teams.add(teamToJoin)
                            }

                            //Remove from existing team if present
                            player.team?.removePlayer(player)

                            //Join new one
                            teamToJoin.addPlayer(player)

                            UHCPlugin.instance.writeTeamData()
                        }

                        return@addItem true
                    },
                    i.toShort(),
                    lore = lore,
                    name = team?.name ?: "${Colors.values[i]?.colorCode}Team ${Colors.values[i]?.name}"
                )
            }

            //Display the GUI
            teamsGui.openFor(event.player)
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        //Only damage done to a player
        if (event.entity !is Player) return

        val player = event.entity as Player

        //Handle dying only
        if (event.damage < player.health) return

        val deathMessage = when (event.cause) {
            EntityDamageEvent.DamageCause.FALL -> "${player.displayName} fell off a cliff."
            EntityDamageEvent.DamageCause.DROWNING -> "${player.displayName} is sleeping with the fish."
            EntityDamageEvent.DamageCause.FALLING_BLOCK -> "${player.displayName} got squished."
            EntityDamageEvent.DamageCause.BLOCK_EXPLOSION -> "${player.displayName} got blown to bits."
            EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK -> "${player.displayName} is roasting on an open fire."
            EntityDamageEvent.DamageCause.LAVA, EntityDamageEvent.DamageCause.MELTING -> "${player.displayName} likes lava a little too much."
            EntityDamageEvent.DamageCause.THORNS -> "${player.displayName} was stabbed by spiky armour"
            EntityDamageEvent.DamageCause.SUFFOCATION -> "${player.displayName} got stuck in a block."
            EntityDamageEvent.DamageCause.POISON -> "${player.displayName} ate something they didn't agree with."
            EntityDamageEvent.DamageCause.WITHER -> "${player.displayName} withered away."
            EntityDamageEvent.DamageCause.LIGHTNING -> "${player.displayName} encountered a once-in-a-lifetime bruh moment."
            EntityDamageEvent.DamageCause.MAGIC -> "${player.displayName} pissed off a witch."
            EntityDamageEvent.DamageCause.STARVATION -> "${player.displayName} didn't pay attention to their hunger bar."
            else -> return
        }

        event.isCancelled = true
        doDeathStuff(player, deathMessage)
    }

    private fun doDeathStuff(player: Player, deathMessage: String) {
        Bukkit.broadcastMessage(deathMessage)

        for (item in player.inventory) {
            if (item != null)
                player.world.dropItemNaturally(player.location, item)
        }

        player.inventory.clear()

        player.playSound(player.location, Sound.HURT_FLESH, 1f, 1f)
        player.gameMode = GameMode.SPECTATOR
    }

    @EventHandler
    fun onDamageByBlock(event: EntityDamageByBlockEvent) {
        //Only damage done to a player
        if (event.entity !is Player) return

        if (event.damager?.isLiquid == true) return

        val player = event.entity as Player

        //Handle dying only
        if (event.finalDamage < player.health) return

        event.isCancelled = true
        val deathMessage = "${player.displayName} died to the environment"


        doDeathStuff(player, deathMessage)
    }

    @EventHandler
    fun onDamageByEntity(event: EntityDamageByEntityEvent) {
        //Handle PVP
        if (event.damager is Player && event.entity is Player) {

            if (UHCPlugin.instance.isAmnesty || !UHCPlugin.instance.hasGameStarted) {
                event.isCancelled = true
                return
            }

            val damager = event.damager as Player
            val damagee = event.entity as Player

            if (damager.team == damagee.team) {
                //Friendly fire
                if (UHCPlugin.instance.disableFriendlyFire) {
                    event.isCancelled = true
                    return
                }
            }
        }

        //Only damage done to a player
        if (event.entity !is Player) return

        val player = event.entity as Player

        //Handle dying
        if (event.finalDamage >= player.health) {
            //Fatal
            event.isCancelled = true
            var deathMessage = "${player.displayName} died to an unknown mob"

            if (event.damager is Player)
                deathMessage = "${player.displayName} was slain by ${(event.damager as Player).displayName}"
            if (event.damager is Monster)
                deathMessage = "${player.displayName} was slain by a ${(event.damager as Monster).name}"
            if (event.damager is FallingBlock || event.damager is LightningStrike) return

            doDeathStuff(player, deathMessage)
        }
    }
}