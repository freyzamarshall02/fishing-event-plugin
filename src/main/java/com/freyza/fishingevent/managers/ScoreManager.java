package com.freyza.fishingevent.managers;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreManager {
    
    private final Map<UUID, Integer> playerScores;
    private final Map<UUID, String> playerNames;
    
    public ScoreManager() {
        this.playerScores = new HashMap<>();
        this.playerNames = new HashMap<>();
    }
    
    public void addScore(Player player, int score) {
        UUID uuid = player.getUniqueId();
        playerScores.put(uuid, playerScores.getOrDefault(uuid, 0) + score);
        playerNames.put(uuid, player.getName());
    }
    
    public int getScore(Player player) {
        return playerScores.getOrDefault(player.getUniqueId(), 0);
    }
    
    public void resetScores() {
        playerScores.clear();
        playerNames.clear();
    }
    
    public List<Map.Entry<UUID, Integer>> getTopPlayers() {
        return playerScores.entrySet()
                .stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }
    
    public String getPlayerName(UUID uuid) {
        return playerNames.getOrDefault(uuid, "Unknown");
    }
    
    public String getTopPlayerName(int rank) {
        List<Map.Entry<UUID, Integer>> topPlayers = getTopPlayers();
        if (rank > 0 && rank <= topPlayers.size()) {
            UUID uuid = topPlayers.get(rank - 1).getKey();
            return getPlayerName(uuid);
        }
        return "";
    }
    
    public int getTopPlayerScore(int rank) {
        List<Map.Entry<UUID, Integer>> topPlayers = getTopPlayers();
        if (rank > 0 && rank <= topPlayers.size()) {
            return topPlayers.get(rank - 1).getValue();
        }
        return 0;
    }
    
    public boolean hasScores() {
        return !playerScores.isEmpty();
    }
    
    public int getPlayerCount() {
        return playerScores.size();
    }
}