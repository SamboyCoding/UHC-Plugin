package me.samboycoding.uhcplugin.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ConfigReloadEvent : Event() {

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        private val handlerList: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}