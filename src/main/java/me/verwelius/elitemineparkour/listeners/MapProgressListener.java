package me.verwelius.elitemineparkour.listeners;

import me.verwelius.elitemineparkour.config.Config;
import me.verwelius.elitemineparkour.parkour.PlayerStorage;
import me.verwelius.elitemineparkour.schemas.MapProgress;
import me.verwelius.elitemineparkour.schemas.ParkourMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MapProgressListener implements Listener {
    private final Config config;
    private final PlayerStorage playerStorage;

    public MapProgressListener(Config config, PlayerStorage playerStorage) {
        this.config = config;
        this.playerStorage = playerStorage;
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getX() == to.getX() && from.getZ() == to.getZ()) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        MapProgress progress = playerStorage.get(uuid);
        if(progress == null) return;

        Map<String, Component> messages = config.messages;
        Map<String, Sound> sounds = config.sounds;
        Map<String, ParkourMap> maps = config.maps;

        ParkourMap map = maps.get(progress.getMapId());

        if(progress.getStartTime() == 0) {
            if(!compare(to, map.getStart())) return;
            progress.setStartTime();
            player.sendMessage(messages.get("parkour-started").replaceText(config -> {
                config.match("%map%").replacement(map.getName());
            }));
            player.playSound(player.getLocation(), sounds.get("parkour-started"), 1, 1);
        }
        else {
            if(compare(to, map.getFinish())) {
                float time = (System.currentTimeMillis() - progress.getStartTime()) / 1000f;
                playerStorage.remove(uuid);
                player.sendMessage(messages.get("parkour-finished")
                        .replaceText(config -> {
                            config.match("%map%").replacement(map.getName());
                        })
                        .replaceText(config -> {
                            config.match("%time%").replacement(String.format("%.3f", time));
                        }));
                player.playSound(player.getLocation(), sounds.get("parkour-finished"), 1, 1);
            }
            else {
                List<Location> points = map.getCheckpoints();
                Location point = points.stream().filter(loc -> {
                    return compare(to, loc);
                }).findAny().orElse(null);
                if(point == null) return;

                int n = points.indexOf(point);
                if(n == progress.getCheckpoint()) return;

                progress.setCheckpoint(n);
                player.sendMessage(messages.get("checkpoint-reached").replaceText(config -> {
                    config.match("%n%").replacement(String.valueOf(n + 1));
                }));
                player.playSound(player.getLocation(), sounds.get("checkpoint-reached"), 1, 1);
            }
        }
    }

    private boolean compare(Location a, Location b) {
        return a.getBlock().getLocation().equals(b.getBlock().getLocation());
        /*
        double deltaX = Math.abs(a.getX() - b.getX());
        double deltaY = Math.abs(a.getY() - b.getY());
        double deltaZ = Math.abs(a.getZ() - b.getZ());
        return deltaX < 1 && deltaY < 1 && deltaZ < 1;
         */
    }

}
