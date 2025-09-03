package com.freyza.fishingevent.managers;

import com.freyza.fishingevent.FishingEventPlugin;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;

public class EventManager {
    
    private final FishingEventPlugin plugin;
    private boolean eventActive;
    private int remainingTime;
    private BukkitTask eventTask;
    private BossBar bossBar;
    
    public EventManager(FishingEventPlugin plugin) {
        this.plugin = plugin;
        this.eventActive = false;
    }
    
    public boolean startEvent(int duration) {
        if (eventActive) {
            return false;
        }
        
        int minPlayers = plugin.getConfigManager().getMinPlayers();
        if (Bukkit.getOnlinePlayers().size() < minPlayers) {
            return false;
        }
        
        eventActive = true;
        remainingTime = duration;
        
        // Reset scores
        plugin.getScoreManager().resetScores();
        
        // Create boss bar
        if (plugin.getConfigManager().isBossBarEnabled()) {
            createBossBar();
        }
        
        // Start countdown task
        startCountdownTask();
        
        // Broadcast start message
        String startMessage = plugin.getConfigManager().getMessage("event-started")
                .replace("%duration%", String.valueOf(duration));
        Bukkit.broadcastMessage(startMessage);
        
        return true;
    }
    
    public void stopEvent() {
        if (!eventActive) {
            return;
        }
        
        eventActive = false;
        
        if (eventTask != null) {
            eventTask.cancel();
        }
        
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar = null;
        }
        
        // Announce winners and give prizes
        announceWinners();
        
        // Broadcast stop message
        String stopMessage = plugin.getConfigManager().getMessage("event-stopped");
        Bukkit.broadcastMessage(stopMessage);
    }
    
    private void createBossBar() {
        String title = plugin.getConfigManager().getBossBarTitle()
                .replace("%time%", String.valueOf(remainingTime));
        
        BarColor color;
        try {
            color = BarColor.valueOf(plugin.getConfigManager().getBossBarColor());
        } catch (IllegalArgumentException e) {
            color = BarColor.GREEN;
        }
        
        BarStyle style;
        try {
            style = BarStyle.valueOf(plugin.getConfigManager().getBossBarStyle());
        } catch (IllegalArgumentException e) {
            style = BarStyle.SOLID;
        }
        
        bossBar = Bukkit.createBossBar(title, color, style);
        bossBar.setProgress(1.0);
        
        // Add all online players to boss bar
        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }
    }
    
    private void startCountdownTask() {
        eventTask = new BukkitRunnable() {
            @Override
            public void run() {
                remainingTime--;
                
                // Update boss bar
                if (bossBar != null) {
                    String title = plugin.getConfigManager().getBossBarTitle()
                            .replace("%time%", String.valueOf(remainingTime));
                    bossBar.setTitle(title);
                    
                    // Calculate progress (remaining time / total time)
                    double progress = Math.max(0.0, (double) remainingTime / (remainingTime + 1));
                    bossBar.setProgress(progress);
                }
                
                if (remainingTime <= 0) {
                    // Event ended naturally
                    eventActive = false;
                    
                    if (bossBar != null) {
                        bossBar.removeAll();
                        bossBar = null;
                    }
                    
                    announceWinners();
                    
                    String endMessage = plugin.getConfigManager().getMessage("event-ended");
                    Bukkit.broadcastMessage(endMessage);
                    
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L); // Run every second
    }
    
    private void announceWinners() {
        if (!plugin.getScoreManager().hasScores()) {
            return;
        }
        
        List<Map.Entry<java.util.UUID, Integer>> topPlayers = plugin.getScoreManager().getTopPlayers();
        Map<Integer, String> prizes = plugin.getConfigManager().getPrizes();
        boolean prizesEnabled = plugin.getConfigManager().isPrizesEnabled();
        
        for (int i = 0; i < Math.min(3, topPlayers.size()); i++) {
            int rank = i + 1;
            java.util.UUID uuid = topPlayers.get(i).getKey();
            int score = topPlayers.get(i).getValue();
            String playerName = plugin.getScoreManager().getPlayerName(uuid);
            
            // Announce winner
            String winnerMessage = plugin.getConfigManager().getMessage("winner-announcement")
                    .replace("%rank%", String.valueOf(rank))
                    .replace("%player%", playerName)
                    .replace("%score%", String.valueOf(score));
            Bukkit.broadcastMessage(winnerMessage);
            
            // Give prize
            if (prizesEnabled && prizes.containsKey(rank)) {
                String command = prizes.get(rank).replace("%player%", playerName);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }
    }
    
    public void addPlayerToBossBar(Player player) {
        if (bossBar != null && eventActive) {
            bossBar.addPlayer(player);
        }
    }
    
    public void removePlayerFromBossBar(Player player) {
        if (bossBar != null) {
            bossBar.removePlayer(player);
        }
    }
    
    public boolean isEventActive() {
        return eventActive;
    }
    
    public int getRemainingTime() {
        return remainingTime;
    }
}