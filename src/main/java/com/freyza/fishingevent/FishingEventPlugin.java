package com.freyza.fishingevent;

import com.freyza.fishingevent.commands.FishEventCommand;
import com.freyza.fishingevent.listeners.FishingListener;
import com.freyza.fishingevent.managers.EventManager;
import com.freyza.fishingevent.managers.ScoreManager;
import com.freyza.fishingevent.placeholders.FishingEventPlaceholders;
import com.freyza.fishingevent.utils.ConfigManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class FishingEventPlugin extends JavaPlugin {
    
    private static FishingEventPlugin instance;
    private ConfigManager configManager;
    private EventManager eventManager;
    private ScoreManager scoreManager;
    private FishingEventPlaceholders placeholders;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        configManager = new ConfigManager(this);
        scoreManager = new ScoreManager();
        eventManager = new EventManager(this);
        
        // Register commands
        registerCommands();
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new FishingListener(this), this);
        
        // Register PlaceholderAPI placeholders
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholders = new FishingEventPlaceholders(this);
            placeholders.register();
            getLogger().info("PlaceholderAPI placeholders registered!");
        } else {
            getLogger().warning("PlaceholderAPI not found! Placeholders will not work.");
        }
        
        getLogger().info("FishingEventPlugin has been enabled!");
    }
    
    @Override
    public void onDisable() {
        if (eventManager != null) {
            eventManager.stopEvent();
        }
        if (placeholders != null) {
            placeholders.unregister();
        }
        getLogger().info("FishingEventPlugin has been disabled!");
    }
    
    private void registerCommands() {
        FishEventCommand commandExecutor = new FishEventCommand(this);
        
        getCommand("fishevent").setExecutor(commandExecutor);
        getCommand("fishstop").setExecutor(commandExecutor);
        getCommand("fishreset").setExecutor(commandExecutor);
        getCommand("fishreload").setExecutor(commandExecutor);
    }
    
    public void reloadPlugin() {
        configManager.reloadConfig();
        getLogger().info("Plugin configuration reloaded!");
    }
    
    // Getters
    public static FishingEventPlugin getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }
    
    public ScoreManager getScoreManager() {
        return scoreManager;
    }
}