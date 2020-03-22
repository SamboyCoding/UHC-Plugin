package me.samboycoding.uhcplugin.utils

import org.bukkit.ChatColor

object Colours {
    data class ColourData(val name: String, val chatColourCode: ChatColor, val woolColourData: Short)

    val values = setOf(
            ColourData("White", ChatColor.WHITE, 0),
            ColourData("Orange", ChatColor.GOLD, 1),
            ColourData("Magenta", ChatColor.LIGHT_PURPLE, 2),
            ColourData("Light Blue", ChatColor.BLUE, 3),
            ColourData("Yellow", ChatColor.YELLOW, 4),
            ColourData("Lime", ChatColor.GREEN, 5),
            ColourData("Pink", ChatColor.RED, 6),
            ColourData("Gray", ChatColor.DARK_GRAY, 7),
            ColourData("Light Gray", ChatColor.GRAY, 8),
            ColourData("Cyan", ChatColor.DARK_AQUA, 9),
            ColourData("Purple", ChatColor.DARK_PURPLE, 10),
            ColourData("Blue", ChatColor.DARK_BLUE, 11),
            ColourData("Green", ChatColor.DARK_GREEN, 13),
            ColourData("Red", ChatColor.DARK_RED, 14),
            ColourData("Black", ChatColor.BLACK, 15)
    )
}