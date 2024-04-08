package me.verwelius.elitemineparkour.listeners;

import me.verwelius.elitemineparkour.config.Config;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MenuListener implements Listener {
    private final Config config;

    public MenuListener(Config config) {
        this.config = config;
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if(event.getView().title() == config.messages.get("map-menu-title")) {
            ItemStack item = event.getCurrentItem();
            if(item == null) return;
            ItemMeta meta = item.getItemMeta();
            if(meta == null) return;

            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            String mapId = pdc.get(new NamespacedKey("parkour", "map-id"), PersistentDataType.STRING);
            if(mapId == null) return;

            Player player = ((Player) event.getWhoClicked());

            player.closeInventory();
            event.setCancelled(true);

            player.chat("/map " + mapId);
        }
    }

}
