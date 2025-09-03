package com.freyza.fishingevent.placeholders;

import com.freyza.fishingevent.FishingEventPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class FishingEventPlaceholders extends PlaceholderExpansion {
    
    private final FishingEventPlugin plugin;
    
    public FishingEventPlaceholders(FishingEventPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getIdentifier() {
        return "fishingevent";
    }
    
    @Override
    public String getAuthor() {
        return "freyza";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (params == null) {
            return null;
        }
        
        // Handle top name placeholders: %fishingevent_top_name_1%
        if (params.startsWith("top_name_")) {
            String rankStr = params.substring("top_name_".length());
            try {
                int rank = Integer.parseInt(rankStr);
                return plugin.getScoreManager().getTopPlayerName(rank);
            } catch (NumberFormatException e) {
                return "";
            }
        }
        
        // Handle top score placeholders: %fishingevent_top_score_1%
        if (params.startsWith("top_score_")) {
            String rankStr = params.substring("top_score_".length());
            try {
                int rank = Integer.parseInt(rankStr);
                int score = plugin.getScoreManager().getTopPlayerScore(rank);
                return score > 0 ? String.valueOf(score) : "";
            } catch (NumberFormatException e) {
                return "";
            }
        }
        
        // Handle player's current score: %fishingevent_player_score%
        if (params.equals("player_score") && player != null) {
            return String.valueOf(plugin.getScoreManager().getScore(player));
        }
        
        // Handle event status: %fishingevent_active%
        if (params.equals("active")) {
            return plugin.getEventManager().isEventActive() ? "true" : "false";
        }
        
        // Handle remaining time: %fishingevent_time_left%
        if (params.equals("time_left")) {
            if (plugin.getEventManager().isEventActive()) {
                return String.valueOf(plugin.getEventManager().getRemainingTime());
            }
            return "0";
        }
        
        // Handle participant count: %fishingevent_participants%
        if (params.equals("participants")) {
            return String.valueOf(plugin.getScoreManager().getPlayerCount());
        }
        
        return null;
    }
}