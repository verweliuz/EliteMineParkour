package me.verwelius.elitemineparkour.commands;

import me.verwelius.elitemineparkour.config.Config;
import me.verwelius.elitemineparkour.parkour.PlayerStorage;
import me.verwelius.elitemineparkour.schemas.ParkourMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapCommand extends AbstractCommand {
    private final Config config;
    private final PlayerStorage playerStorage;

    public MapCommand(Config config, PlayerStorage playerStorage) {
        this.config = config;
        this.playerStorage = playerStorage;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        Map<String, Component> messages = config.messages;

        if(!(sender instanceof Player player)) {
            sender.sendMessage(messages.get("command-for-players"));
            return;
        }

        Map<String, Sound> sounds = config.sounds;

        if(args.length != 1) {
            sender.sendMessage(messages.get("syntax-error"));
            player.playSound(player.getLocation(), sounds.get("command-error"), 1, 1);
            return;
        }

        ParkourMap map = config.maps.get(args[0]);
        if(map == null) {
            sender.sendMessage(messages.get("non-existent-map"));
            player.playSound(player.getLocation(), sounds.get("command-error"), 1, 1);
            return;
        }

        player.teleport(map.getTeleport());
        playerStorage.put(player.getUniqueId(), args[0]);
        player.playSound(player.getLocation(), sounds.get("teleported"), 1, 1);
    }

    @Override
    protected List<String> suggest(CommandSender sender, String[] args) {
        return args.length == 1 ? config.maps.keySet().stream().toList() : new ArrayList<>();
    }

}
