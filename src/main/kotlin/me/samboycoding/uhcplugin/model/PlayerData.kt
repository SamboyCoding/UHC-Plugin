package me.samboycoding.uhcplugin.model

import java.util.*

class PlayerData(val uuid: UUID) {
    var kills = arrayListOf<KillData>()
    var death: KillData? = null

    data class KillData(val time: Long, val killer: UUID, val victim: UUID)
}