package com.freyza.fishingevent.utils;

import com.freyza.fishingevent.FishingEventPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    
    private final FishingEventPlugin plugin;
    private FileConfiguration config;
    
    public ConfigManager(FishingEventPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
    
    public int getMinPlayers() {
        return config.getInt("min-players", 2);
    }
    
    public boolean isPrizesEnabled() {
        return config.getBoolean("prizes-enabled", true);
    }
    
    public Map<Integer, String> getPrizes() {
        Map<Integer, String> prizes = new HashMap<>();
        if (config.contains("prizes")) {
            for (String key : config.getConfigurationSection("prizes").getKeys(false)) {
                try {
                    int rank = Integer.parseInt(key);
                    String command = config.getString("prizes." + key);
                    prizes.put(rank, command);
                } catch (NumberFormatException e) {
                    plugin.getLogger().warning("Invalid prize rank: " + key);
                }
            }
        }
        return prizes;
    }
    
    public Map<EntityType, Integer> getFishScores() {
        Map<EntityType, Integer> scores = new HashMap<>();
        if (config.contains("fish-scores")) {
            for (String key : config.getConfigurationSection("fish-scores").getKeys(false)) {
                try {
                    EntityType fishType = EntityType.valueOf(key.toUpperCase());
                    int score = config.getInt("fish-scores." + key);
                    scores.put(fishType, score);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid fish type: " + key);
                }
            }
        }
        return scores;
    }
    
    public String getMessage(String key) {
        String message = config.getString("messages." + key, "&cMessage not found: " + key);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public boolean isBossBarEnabled() {
        return config.getBoolean("bossbar.enabled", true);
    }
    
    public String getBossBarTitle() {
        String title = config.getString("bossbar.title", "&a&lFishing Event &f- &b%time% &fseconds left");
        return ChatColor.translateAlternateColorCodes('&', title);
    }
    
    public String getBossBarColor() {
        return config.getString("bossbar.color", "GREEN");
    }
    
    public String getBossBarStyle() {
        return config.getString("bossbar.style", "SOLID");
    }
}