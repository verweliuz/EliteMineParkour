package me.verwelius.elitemineparkour;

import me.verwelius.elitemineparkour.commands.AbstractCommand;
import me.verwelius.elitemineparkour.commands.MapCommand;
import me.verwelius.elitemineparkour.commands.MenuCommand;
import me.verwelius.elitemineparkour.config.Config;
import me.verwelius.elitemineparkour.listeners.MapProgressListener;
import me.verwelius.elitemineparkour.listeners.MenuListener;
import me.verwelius.elitemineparkour.parkour.PlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class EliteMineParkour extends JavaPlugin {

    @Override
    public void onEnable() {
        PlayerStorage playerStorage = new PlayerStorage();
        Config config = new Config(this);

        registerListener(new MenuListener(config));
        registerListener(new MapProgressListener(config, playerStorage));

        registerCommand("menu", new MenuCommand(config));
        registerCommand("map", new MapCommand(config, playerStorage));
    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void registerCommand(String name, AbstractCommand handler) {
        PluginCommand command = getCommand(name);
        if(command == null) return;
        command.setExecutor(handler);
        command.setTabCompleter(handler);
    }

}
