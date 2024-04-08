package me.verwelius.elitemineparkour.parkour;

import me.verwelius.elitemineparkour.schemas.MapProgress;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStorage {
    private final Map<UUID, MapProgress> players = new HashMap<>();

    public MapProgress get(UUID uuid) {
        return players.get(uuid);
    }

    public void put(UUID uuid, String mapId) {
        players.put(uuid, new MapProgress(mapId));
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }

}
