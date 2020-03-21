package me.samboycoding.uhcplugin.game

import me.samboycoding.uhcplugin.UHCPlugin
import me.samboycoding.uhcplugin.events.GameStartEvent
import me.samboycoding.uhcplugin.events.GameStopEvent

class UHCGame {

    var isRunning: Boolean = false
     set(running) {
         if (running) {
            UHCPlugin.instance.server.pluginManager.callEvent(GameStartEvent())
         } else {
             UHCPlugin.instance.server.pluginManager.callEvent(GameStopEvent())
         }

         field = running
     }


}