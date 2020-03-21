package me.samboycoding.uhcplugin.listeners

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.events.GameStartEvent
import me.samboycoding.uhcplugin.events.GameStopEvent
import me.samboycoding.uhcplugin.game.Team
import net.minecraft.server.v1_8_R3.EnchantmentProtection
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class LobbyListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        UHCPlugin.instance.game.getPlayerTeam(event.player)?.setNameColour(event.player)
        if (!UHCPlugin.instance.game.isRunning) {
            UHCPlugin.instance.logger.info("Player joined, handling inventory")
            setupInventory(event.player)
        }
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (UHCPlugin.instance.game.isRunning || event.currentItem.amount == 0) { return }
        val player: Player = event.whoClicked as Player
        event.isCancelled = true
        UHCPlugin.instance.game.getPlayerTeam(player)?.removePlayer(player)
        UHCPlugin.instance.game.teams.first { it.colour.woolColourData == event.currentItem?.durability }.addPlayer(player)
        setupInventory(player)
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        if (!UHCPlugin.instance.game.isRunning) {
            event.isCancelled = true
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

    private fun setupInventory(player: Player) {
        player.inventory.clear()
        UHCPlugin.instance.game.teams.forEachIndexed { i: Int, team: Team ->
            val stack = ItemStack(Material.WOOL, 1, team.colour.woolColourData)
            val meta: ItemMeta = stack.itemMeta
            meta.displayName = "${team.colour.chatColourCode}${team.colour.name}"
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            if (UHCPlugin.instance.game.getPlayerTeam(player) == team) {
                meta.lore = listOf("${ChatColor.GREEN}You are on this team")
                meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true)
            } else {
                meta.lore = listOf("${ChatColor.WHITE}Click to join this team")
            }
            stack.itemMeta = meta
            player.inventory.setItem(9+i, stack)
        }
    }

}