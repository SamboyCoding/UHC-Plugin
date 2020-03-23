package me.samboycoding.uhcplugin.game

import me.samboycoding.uhcplugin.UHCPlugin
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.material.MaterialData

class RecipeManager {

    fun addGoldenHeadRecipe() {
        val stack = ItemStack(Material.SKULL_ITEM, 1, 3)
        val meta = stack.itemMeta as SkullMeta
        meta.displayName = "${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}Golden Head"
        meta.owner = "StackedGold"
        stack.itemMeta = meta

        val recipe = ShapedRecipe(stack)
        recipe.shape("***", "*h*", "***")
        recipe.setIngredient('*', Material.GOLD_INGOT)
        @Suppress("DEPRECATION")
        recipe.setIngredient('h', MaterialData(Material.SKULL_ITEM, 3))


        UHCPlugin.instance.server.addRecipe(recipe)
    }
}