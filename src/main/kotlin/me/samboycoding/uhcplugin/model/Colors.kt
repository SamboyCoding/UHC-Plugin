package me.samboycoding.uhcplugin.model

import org.bukkit.ChatColor

object Colors {
    data class ColorData(val name: String, val colorCode: ChatColor)

    val values = mapOf(
        Pair(0, ColorData("White", ChatColor.WHITE)),
        Pair(1, ColorData("Orange", ChatColor.GOLD)),
        Pair(2, ColorData("Magenta", ChatColor.LIGHT_PURPLE)),
        Pair(3, ColorData("Light Blue", ChatColor.BLUE)),
        Pair(4, ColorData("Yellow", ChatColor.YELLOW)),
        Pair(5, ColorData("Lime", ChatColor.GREEN)),
        Pair(6, ColorData("Pink", ChatColor.RED)),
        Pair(7, ColorData("Gray", ChatColor.DARK_GRAY)),
        Pair(8, ColorData("Light Gray", ChatColor.GRAY)),
        Pair(9, ColorData("Cyan", ChatColor.DARK_AQUA)),
        Pair(10, ColorData("Purple", ChatColor.DARK_PURPLE)),
        Pair(11, ColorData("Blue", ChatColor.DARK_BLUE)),
        Pair(13, ColorData("Green", ChatColor.DARK_GREEN)),
        Pair(14, ColorData("Red", ChatColor.DARK_RED)),
        Pair(15, ColorData("Black", ChatColor.BLACK))
    )

}