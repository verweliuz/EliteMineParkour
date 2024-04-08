package me.verwelius.elitemineparkour.commands;

import me.verwelius.elitemineparkour.config.Config;
import me.verwelius.elitemineparkour.schemas.ParkourMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuCommand extends AbstractCommand {
    private final Config config;

    public MenuCommand(Config config) {
        this.config = config;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        Map<String, Component> messages = config.messages;

        if(!(sender instanceof Player player)) {
            sender.sendMessage(messages.get("command-for-players"));
            return;
        }

        Map<String, Sound> sounds = config.sounds;

        if(args.length != 0) {
            sender.sendMessage(messages.get("syntax-error"));
            player.playSound(player.getLocation(), sounds.get("command-error"), 1, 1);
            return;
        }

        List<Map.Entry<String, ParkourMap>> maps = config.maps.entrySet().stream().toList();

        Inventory inventory = Bukkit.createInventory(null, 45, messages.get("map-menu-title"));
        for(int i = 0; i < 45; i++) {
            if(maps.size() <= i) break;
            ItemStack item = new ItemStack(maps.get(i).getValue().getIcon());
            ItemMeta meta = item.getItemMeta();
            meta.displayName(maps.get(i).getValue().getName());
            meta.getPersistentDataContainer().set(
                    new NamespacedKey("parkour", "map-id"),
                    PersistentDataType.STRING,
                    maps.get(i).getKey()
            );
            item.setItemMeta(meta);
            inventory.setItem(i, item);
        }

        player.openInventory(inventory);
    }

    @Override
    protected List<String> suggest(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
