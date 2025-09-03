package com.freyza.fishingevent.commands;

import com.freyza.fishingevent.FishingEventPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FishEventCommand implements CommandExecutor {
    
    private final FishingEventPlugin plugin;
    
    public FishEventCommand(FishingEventPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!sender.hasPermission("fishingevent.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        
        String cmdName = command.getName().toLowerCase();
        
        switch (cmdName) {
            case "fishevent":
                return handleFishEvent(sender, args);
            case "fishstop":
                return handleFishStop(sender);
            case "fishreset":
                return handleFishReset(sender);
            case "fishreload":
                return handleFishReload(sender);
            default:
                return false;
        }
    }
    
    private boolean handleFishEvent(CommandSender sender, String[] args) {
        if (args.length < 2 || !args[0].equalsIgnoreCase("start")) {
            sender.sendMessage("§cUsage: /fishevent start <duration>");
            return true;
        }
        
        int duration;
        try {
            duration = Integer.parseInt(args[1]);
            if (duration <= 0) {
                sender.sendMessage("§cDuration must be a positive number!");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid duration! Please enter a number.");
            return true;
        }
        
        if (plugin.getEventManager().isEventActive()) {
            String message = plugin.getConfigManager().getMessage("already-running");
            sender.sendMessage(message);
            return true;
        }
        
        boolean started = plugin.getEventManager().startEvent(duration);
        if (!started) {
            String message = plugin.getConfigManager().getMessage("not-enough-players")
                    .replace("%min%", String.valueOf(plugin.getConfigManager().getMinPlayers()));
            sender.sendMessage(message);
            return true;
        }
        
        return true;
    }
    
    private boolean handleFishStop(CommandSender sender) {
        if (!plugin.getEventManager().isEventActive()) {
            String message = plugin.getConfigManager().getMessage("no-event-running");
            sender.sendMessage(message);
            return true;
        }
        
        plugin.getEventManager().stopEvent();
        return true;
    }
    
    private boolean handleFishReset(CommandSender sender) {
        plugin.getScoreManager().resetScores();
        String message = plugin.getConfigManager().getMessage("leaderboard-reset");
        sender.sendMessage(message);
        return true;
    }
    
    private boolean handleFishReload(CommandSender sender) {
        plugin.reloadPlugin();
        String message = plugin.getConfigManager().getMessage("config-reloaded");
        sender.sendMessage(message);
        return true;
    }
}