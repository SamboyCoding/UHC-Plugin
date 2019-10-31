package me.samboycoding.uhcplugin

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.jetbrains.annotations.NotNull


class GenericGui(size: Int, title: String) : InventoryHolder, Listener {
    private val inv: Inventory = Bukkit.createInventory(this, size, title)
    private val callbacks = HashMap<Material, (ItemStack, Player) -> Boolean>()
    private var player: Player? = null

    init {
        UHCPlugin.instance.server.pluginManager.registerEvents(this, UHCPlugin.instance)
    }


    @NotNull
    override fun getInventory(): Inventory {
        return inv
    }

    fun addItem(
        material: Material,
        callback: (ItemStack, Player) -> Boolean,
        meta: Short = 0,
        name: String? = null,
        lore: ArrayList<String>
    ): GenericGui {
        val stack = createGuiItem(
            material, meta, name, lore
        )

        inv.addItem(stack)

        callbacks[stack.type] = callback

        return this
    }

    // Nice little method to create a gui item with a custom name, and description
    private fun createGuiItem(material: Material, mDataNum: Short, name: String?, lore: ArrayList<String>): ItemStack {
        val item = ItemStack(material, 1)

        val meta = item.itemMeta
        meta.displayName = name
        val loreLines = ArrayList<String>()

        for (line in lore) {
            loreLines.add(line)
        }

        meta.lore = loreLines
        item.itemMeta = meta
        item.durability = mDataNum
        return item
    }

    // You can open the inventory with this
    fun openFor(p: Player) {
        p.openInventory(inv)
        player = p
    }

    // Check for clicks on items
    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory.holder != this || e.whoClicked != this.player) {
            return
        }
        if (e.click == ClickType.NUMBER_KEY) {
            e.isCancelled = true
        }
        e.isCancelled = true

        val p = e.whoClicked as Player
        val clickedItem = e.currentItem

        // verify current item is not null
        if (clickedItem == null || clickedItem.type == Material.AIR) return

        val result = callbacks[clickedItem.type]?.invoke(clickedItem, p) ?: false
        if (result) {
            InventoryClickEvent.getHandlerList().unregister(this)
            InventoryCloseEvent.getHandlerList().unregister(this)
            p.closeInventory()
        }

        e.isCancelled = true
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        if (e.inventory.holder == this && e.player == this.player) {
            InventoryClickEvent.getHandlerList().unregister(this)
            InventoryCloseEvent.getHandlerList().unregister(this)
            player = null
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is GenericGui && other.player == player
    }

    override fun toString(): String {
        return super.toString() + " - Registered to $player"
    }
}