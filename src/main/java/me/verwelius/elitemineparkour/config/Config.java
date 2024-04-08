package me.verwelius.elitemineparkour.config;

import me.verwelius.elitemineparkour.schemas.ParkourMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final Plugin plugin;

    private FileConfiguration config;

    public Map<String, Component> messages;
    public Map<String, Sound> sounds;
    public Map<String, ParkourMap> maps;

    public Config(Plugin plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        config = plugin.getConfig();

        messages = getMessages();
        sounds = getSounds();
        maps = getMaps();
    }

    private World getWorld() {
        return Bukkit.getWorld(config.getString("world", "world"));
    }

    private Map<String, Component> getMessages() {
        Map<String, Component> messages = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("messages");
        if(section == null) return messages;

        section.getKeys(false).forEach(key -> {
            messages.put(key, getComponent(section.getCurrentPath() + "." + key));
        });

        return messages;
    }

    private Map<String, Sound> getSounds() {
        Map<String, Sound> sounds = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("sounds");
        if(section == null) return sounds;

        section.getKeys(false).forEach(key -> {
            sounds.put(key, getSound(section.getCurrentPath() + "." + key));
        });

        return sounds;
    }

    private Map<String, ParkourMap> getMaps() {
        Map<String, ParkourMap> maps = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("maps");
        if(section == null) return maps;

        section.getKeys(false).forEach(key -> {
            maps.put(key, getMap(key));
        });

        return maps;
    }

    private ParkourMap getMap(String id) {
        String path = "maps." + id + ".";

        List<String> list = config.getStringList(path + "checkpoints");
        List<Location> checkpoints = new ArrayList<>();
        list.forEach(s -> checkpoints.add(parseLocation(s)));

        return new ParkourMap(
                getMaterial(path + "icon"),
                getComponent(path + "name"),
                getLocation(path + "teleport"),
                getLocation(path + "start-at"),
                getLocation(path + "finish-at"),
                checkpoints);
    }

    private Component getComponent(String path) {
        return parseComponent(config.getString(path, "error"));
    }

    private Component parseComponent(String input) {
        Component reset = Component.empty().style(builder -> {
            builder.color(NamedTextColor.WHITE);
            builder.decoration(TextDecoration.BOLD, false);
            builder.decoration(TextDecoration.ITALIC, false);
            builder.decoration(TextDecoration.UNDERLINED, false);
            builder.decoration(TextDecoration.OBFUSCATED, false);
            builder.decoration(TextDecoration.STRIKETHROUGH, false);
        });
        return reset.append(MINI_MESSAGE.deserialize(input));
    }

    private Material getMaterial(String path) {
        return Material.valueOf(config.getString(path));
    }
    private Sound getSound(String path) {
        return Sound.valueOf(config.getString(path));
    }

    private Location getLocation(String path) {
        return parseLocation(config.getString(path, "0 0 0"));
    }

    private Location parseLocation(String input) {
        String[] args = input.split(" ");
        if(args.length == 3) {
            return new Location(getWorld(),
                    Float.parseFloat(args[0]),
                    Float.parseFloat(args[1]),
                    Float.parseFloat(args[2])
            );
        }
        if(args.length == 5) {
            return new Location(getWorld(),
                    Float.parseFloat(args[0]),
                    Float.parseFloat(args[1]),
                    Float.parseFloat(args[2]),
                    Float.parseFloat(args[3]),
                    Float.parseFloat(args[4])
            );
        }
        return null;
    }

}
