package me.samboycoding.uhcplugin.listeners

import me.samboycoding.uhcplugin.UHCPlugin
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

class CraftingListener : Listener {

    @EventHandler
    fun onCraft(event: CraftItemEvent) {
        if (isGoldenHead(event.recipe.result) && event.clickedInventory.contents.slice(1..9).find { isGoldenHead(it) } != null) {
            event.isCancelled = true
            event.whoClicked.sendMessage("${ChatColor.RED}Don't waste your gold!")
        }
    }

    private fun isGoldenHead(stack: ItemStack): Boolean {
        return stack.type == Material.SKULL_ITEM && stack.itemMeta.displayName == "${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}Golden Head"
    }
}