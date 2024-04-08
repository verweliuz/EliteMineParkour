package me.verwelius.elitemineparkour.schemas;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public class ParkourMap {
    private final Material icon;
    private final Component name;

    private final Location teleport;
    private final Location start;
    private final Location finish;

    private final List<Location> checkpoints;

    public ParkourMap(Material icon, Component name, Location teleport, Location start, Location finish, List<Location> checkpoints) {
        this.icon = icon;
        this.name = name;
        this.teleport = teleport;
        this.start = start;
        this.finish = finish;
        this.checkpoints = checkpoints;
    }

    public Material getIcon() {
        return icon;
    }

    public Component getName() {
        return name;
    }

    public Location getTeleport() {
        return teleport;
    }

    public Location getStart() {
        return start;
    }

    public Location getFinish() {
        return finish;
    }

    public List<Location> getCheckpoints() {
        return checkpoints;
    }
}
