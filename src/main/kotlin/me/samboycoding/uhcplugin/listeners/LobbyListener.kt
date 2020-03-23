package me.samboycoding.uhcplugin.listeners

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.events.GameStartEvent
import me.samboycoding.uhcplugin.events.GameStopEvent
import me.samboycoding.uhcplugin.events.PlayerTeamSwitchEvent
import me.samboycoding.uhcplugin.game.Team
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class LobbyListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
//        UHCPlugin.instance.game.getPlayerTeam(event.player)?.setNameColour(event.player)
        if (!UHCPlugin.instance.game.isRunning) {
            event.player.playerListName = "${ChatColor.GRAY}${ChatColor.ITALIC}${event.player.name}${ChatColor.RESET}"
            event.player.displayName = "${ChatColor.GRAY}${ChatColor.ITALIC}${event.player.name}${ChatColor.RESET}"
            UHCPlugin.instance.logger.info("Player joined, handling inventory")
            setupInventory(event.player)
            event.player.gameMode = GameMode.ADVENTURE
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        if (!UHCPlugin.instance.game.isRunning) {
            UHCPlugin.instance.game.getPlayerTeam(event.player)?.removePlayer(event.player)
        }
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (UHCPlugin.instance.game.isRunning || event.currentItem == null || event.currentItem.amount == 0) {
            return
        }
        val player: Player = event.whoClicked as Player

        if (player.gameMode == GameMode.CREATIVE) {
            return
        }

        event.isCancelled = true
        val team: Team = UHCPlugin.instance.game.teams.first { it.colour.woolColourData == event.currentItem?.durability }
        if (team.playerIDs.size >= UHCPlugin.instance.config.getInt("game.team_size")) {
            return
        }
        UHCPlugin.instance.game.getPlayerTeam(player)?.removePlayer(player)
        team.addPlayer(player)
        setupInventory(player)
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        if (!UHCPlugin.instance.game.isRunning) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPickup(event: PlayerPickupItemEvent) {
        if (!UHCPlugin.instance.game.isRunning) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        if (!UHCPlugin.instance.game.isRunning) {
            event.entity.health = event.entity.maxHealth
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (!UHCPlugin.instance.game.isRunning && event.entity is Player) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onHunger(event: FoodLevelChangeEvent) {
        if (!UHCPlugin.instance.game.isRunning && event.entity is Player) {
            (event.entity as Player).foodLevel = 20
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEntityEvent) {
        if (!UHCPlugin.instance.game.isRunning && event.player.isSneaking && event.rightClicked is Player) {
            event.player.passenger = event.rightClicked
        }
    }

    @EventHandler
    fun onPlayerClick(event: PlayerInteractEvent) {
        if (!UHCPlugin.instance.game.isRunning && event.player.passenger != null && event.action == Action.LEFT_CLICK_AIR) {
            val player = event.player.passenger
            event.player.eject()
            player.velocity = event.player.location.direction.multiply(1.5)
        }
    }

    @EventHandler
    fun onGameStart(event: GameStartEvent) {
        UHCPlugin.instance.server.onlinePlayers.forEach { it.inventory.clear() }
    }

    @EventHandler
    fun onGameEnd(event: GameStopEvent) {
        UHCPlugin.instance.server.onlinePlayers.forEach { setupInventory(it) }
    }

    @EventHandler
    fun onTeamSwitch(event: PlayerTeamSwitchEvent) {
        UHCPlugin.instance.server.onlinePlayers.forEach { setupInventory(it) }
    }

    private fun setupInventory(player: Player) {
        player.inventory.clear()
        UHCPlugin.instance.game.teams.forEachIndexed { i: Int, team: Team ->
            val teamFull: Boolean = team.playerIDs.size >= UHCPlugin.instance.config.getInt("game.team_size")
            val memberOfTeam: Boolean = UHCPlugin.instance.game.getPlayerTeam(player) == team

            val stack = ItemStack(when {
                memberOfTeam -> Material.WOOL
                teamFull -> Material.BARRIER
                else -> Material.STAINED_GLASS
            }, 1, team.colour.woolColourData)
            val meta: ItemMeta = stack.itemMeta
            meta.displayName = "${team.colour.chatColourCode}${team.colour.name}"
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            when {
                memberOfTeam -> {
                    meta.lore = listOf("${ChatColor.GREEN}You are on this team")
                    meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true)
                }
                teamFull -> {
                    meta.lore = listOf("${ChatColor.RED}This team is full")
                }
                else -> {
                    meta.lore = listOf("${ChatColor.WHITE}Click to join this team")
                }
            }
            stack.itemMeta = meta
            player.inventory.setItem(9 + i, stack)
        }
    }

}