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
        
        // Debug: Log what was caught
        plugin.getLogger().info("Player " + player.getName() + " caught: " + event.getCaught().getType());
        
        EntityType caughtType = event.getCaught().getType();
        Map<EntityType, Integer> fishScores = plugin.getConfigManager().getFishScores();
        
        // For items (fish items), we need to handle differently
        if (caughtType == EntityType.ITEM) {
            org.bukkit.entity.Item item = (org.bukkit.entity.Item) event.getCaught();
            org.bukkit.Material itemType = item.getItemStack().getType();
            
            // Convert material to entity type for scoring
            EntityType fishType = materialToFishType(itemType);
            if (fishType != null && fishScores.containsKey(fishType)) {
                int score = fishScores.get(fishType);
                plugin.getScoreManager().addScore(player, score);
                
                // Format fish name for display
                String fishName = formatFishName(fishType.name());
                
                // Send catch announcement
                String catchMessage = plugin.getConfigManager().getMessage("fish-caught")
                        .replace("%player%", player.getName())
                        .replace("%fish%", fishName)
                        .replace("%score%", String.valueOf(score));
                
                Bukkit.broadcastMessage(catchMessage);
                return;
            }
        }
        
        // Check if this fish type has a score value (for direct entity catches)
        if (fishScores.containsKey(caughtType)) {
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
    }
    
    /**
     * Convert material type to corresponding fish entity type
     */
    private EntityType materialToFishType(org.bukkit.Material material) {
        switch (material) {
            case COD:
                return EntityType.COD;
            case SALMON:
                return EntityType.SALMON;
            case TROPICAL_FISH:
                return EntityType.TROPICAL_FISH;
            case PUFFERFISH:
                return EntityType.PUFFERFISH;
            default:
                return null;
        }
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