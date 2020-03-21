package me.samboycoding.uhcplugin.listeners

import me.samboycoding.uhcplugin.UHCPlugin
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent

class PortalListener : Listener {

    @EventHandler
    fun onPortalUse(event: PlayerPortalEvent) {
        val gameWorldName: String = UHCPlugin.instance.config.getString("game.game_world")
        if (event.from.world.name == gameWorldName) {
            if (event.cause == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
                val destinationPortal: Location = event.portalTravelAgent.findOrCreate(Location(
                        UHCPlugin.instance.server.getWorld("${gameWorldName}_nether"),
                        event.player.location.x,
                        event.player.location.y,
                        event.player.location.z
                ))
                UHCPlugin.instance.server.broadcastMessage(destinationPortal.toString())
                event.to = destinationPortal
                UHCPlugin.instance.server.broadcastMessage("${ChatColor.YELLOW}${event.player.name} has entered the Nether!")
            } else if (event.cause == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
                event.player.teleport(UHCPlugin.instance.server.getWorld("${gameWorldName}_the_end").getHighestBlockAt(0, 0).location)
                UHCPlugin.instance.server.broadcastMessage("${ChatColor.YELLOW}${event.player.name} has entered the End!")
            }
        } else if (event.from.world.name == "${gameWorldName}_nether") {
            val destinationPortal: Location = event.portalTravelAgent.findOrCreate(Location(
                    UHCPlugin.instance.server.getWorld(gameWorldName),
                    event.player.location.x,
                    event.player.location.y,
                    event.player.location.z
            ))
            event.to = destinationPortal
            event.player.teleport(destinationPortal, PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
        }
    }
}