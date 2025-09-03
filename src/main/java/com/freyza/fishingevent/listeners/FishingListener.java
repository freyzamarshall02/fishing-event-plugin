package com.freyza.fishingevent.listeners;

import com.freyza.fishingevent.FishingEventPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class FishingListener implements Listener {
    
    private final FishingEventPlugin plugin;
    
    public FishingListener(FishingEventPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // Only process if event is active
        if (!plugin.getEventManager().isEventActive()) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // Check if player has permission to participate
        if (!player.hasPermission("fishingevent.participate")) {
            return;
        }
        
        // Only count successfully caught fish
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }
        
        // Ensure something was actually caught
        if (event.getCaught() == null) {
            return;
        }
        
        EntityType caughtType = event.getCaught().getType();
        Map<EntityType, Integer> fishScores = plugin.getConfigManager().getFishScores();
        
        // Check if this fish type has a score value
        if (!fishScores.containsKey(caughtType)) {
            return; // Not a scored fish type
        }
        
        int score = fishScores.get(caughtType);
        plugin.getScoreManager().addScore(player, score);
        
        // Format fish name for display
        String fishName = formatFishName(caughtType.name());
        
        // Send catch announcement
        String catchMessage = plugin.getConfigManager().getMessage("fish-caught")
                .replace("%player%", player.getName())
                .replace("%fish%", fishName)
                .replace("%score%", String.valueOf(score));
        
        Bukkit.broadcastMessage(catchMessage);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Add player to boss bar if event is currently active
        if (plugin.getEventManager().isEventActive()) {
            plugin.getEventManager().addPlayerToBossBar(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove player from boss bar when they disconnect
        plugin.getEventManager().removePlayerFromBossBar(event.getPlayer());
    }
    
    /**
     * Formats fish entity type names for better display
     * @param fishTypeName The raw entity type name
     * @return Formatted fish name
     */
    private String formatFishName(String fishTypeName) {
        return fishTypeName.toLowerCase()
                .replace("_", " ")
                .replace("fish", "fish")
                .trim();
    }
}