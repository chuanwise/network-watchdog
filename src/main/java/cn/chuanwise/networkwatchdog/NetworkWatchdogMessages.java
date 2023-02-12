package cn.chuanwise.networkwatchdog;

import cn.chuanwise.networkwatchdog.util.Arguments;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;

/**
 * Messages
 *
 * @author Chuanwise
 */
public class NetworkWatchdogMessages {
    
    private final FileConfiguration configuration;
    
    public NetworkWatchdogMessages(FileConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public String getFormat(String path) {
        return configuration.getString(path);
    }
    
    public String getFormatOrFail(String path) {
        final String string = configuration.getString(path);
        if (string == null || string.isEmpty()) {
            throw new NoSuchElementException("Illegal sentence:" + path);
        }
        return string;
    }
    
    public String getFormatOrDefault(String path) {
        return configuration.getString(path, path);
    }
    
    public String format(String key) {
        final String string = configuration.getString(key);
        if (string == null || string.isEmpty()) {
            return key;
        }
    
        return PlaceholderAPI.setPlaceholders(null, string);
    }
    
    public String format(String key, Object... arguments) {
        final String string = configuration.getString(key);
        if (string == null || string.isEmpty()) {
            return key;
        }
    
        return PlaceholderAPI.setPlaceholders(null, Arguments.format(string, arguments));
    }
    
    public String format(OfflinePlayer offlinePlayer, String key) {
        final String string = configuration.getString(key);
        if (string == null || string.isEmpty()) {
            return key;
        }
    
        return PlaceholderAPI.setPlaceholders(offlinePlayer, string);
    }
    
    public String format(OfflinePlayer offlinePlayer, String key, Object... arguments) {
        final String string = configuration.getString(key);
        if (string == null || string.isEmpty()) {
            return key;
        }
    
        return PlaceholderAPI.setPlaceholders(offlinePlayer, Arguments.format(string, arguments));
    }
    
    public String format(Player player, String key) {
        final String string = configuration.getString(key);
        if (string == null || string.isEmpty()) {
            return key;
        }
    
        return PlaceholderAPI.setPlaceholders(player, string);
    }
    
    public String format(Player player, String key, Object... arguments) {
        final String string = configuration.getString(key);
        if (string == null || string.isEmpty()) {
            return key;
        }
    
        return PlaceholderAPI.setPlaceholders(player, Arguments.format(string, arguments));
    }
}
