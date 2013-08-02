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
 
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
 
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
        if (player.hasPermission("levelhealth.use") || !getConfig().getBoolean("use-permissions")) {
            int lvl = player.getLevel();
            int maxHP = 20;
            if (maxHPConfig.containsKey(lvl)) {
                maxHP = maxHPConfig.get(lvl);
            }
            player.setMaxHealth(maxHP);
        }
    }
}
