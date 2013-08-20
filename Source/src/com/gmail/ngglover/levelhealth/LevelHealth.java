package com.gmail.ngglover.levelhealth;
 
 
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class LevelHealth extends JavaPlugin implements Listener {
 
    HashMap<Integer, Integer> maxHPConfig = new HashMap<Integer, Integer>();
    boolean usePermissions = false;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        reloadConfig();
    }
 
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        usePermissions = getConfig().getBoolean("use-permissions", false);
        maxHPConfig.clear();
        for (String key: getConfig().getConfigurationSection("max-hp-per-level").getKeys(false)) {
            maxHPConfig.put(Integer.parseInt(key), getConfig().getInt("max-hp-per-level." + key));
        }
    }

    @EventHandler
    public void onPlayerXPLevelChange(PlayerLevelChangeEvent event) {
        final Player player = event.getPlayer();
        getServer().getScheduler().runTaskLater(this, new Runnable() {
    				public void run() {
    					scaleHealth(player);
    				}
    			}, 1);
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        scaleHealth(event.getPlayer());
    }
 
    private void scaleHealth(Player player) {
        if (!usePermissions || player.hasPermission("levelhealth.use")) {
            int lvl = player.getLevel();
            int maxHP = 20;
            for (int i=lvl; i>=0; i--) {
                if (maxHPConfig.containsKey(i)) {
                    maxHP = maxHPConfig.get(i);
                    break;
                }
            }
            player.setMaxHealth(maxHP);
        }
    }
}
