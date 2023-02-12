package cn.chuanwise.networkwatchdog;

import cn.chuanwise.networkwatchdog.util.Exceptions;
import cn.chuanwise.networkwatchdog.util.Loggers;
import cn.chuanwise.networkwatchdog.util.Resources;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class NetworkWatchdogPlugin
    extends JavaPlugin {
    
    private static NetworkWatchdogPlugin instance;
    
    private volatile NetworkWatchdogConfiguration configuration;
    private volatile NetworkWatchdogMessages messages;
    
    public static NetworkWatchdogPlugin getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Plugin hadn't been loaded!");
        }
        return instance;
    }
    
    @Override
    public void onLoad() {
        instance = this;
    }
    
    public NetworkWatchdogConfiguration getConfiguration() {
        return configuration;
    }
    
    public NetworkWatchdogMessages getMessages() {
        return messages;
    }
    
    @Override
    public void onEnable() {
        configuration = null;
    
        loadConfiguration();
        loadMessages();
    
        // register commands
        final PluginCommand pluginCommand = getCommand("network-watchdog");
        Objects.requireNonNull(pluginCommand, "Plugin command is null!");
        pluginCommand.setExecutor(NetworkWatchdogCommand.getInstance());
        
        getServer().getScheduler().runTaskTimerAsynchronously(this, NetworkWatchdogTask.getInstance(), 0, 1);
    }
    
    public void loadConfiguration() {
        final File dataFolder = getDataFolder();
        if (!dataFolder.isDirectory() && !dataFolder.mkdirs()) {
            throw new IllegalStateException("Can not create data folder: " + dataFolder.getAbsolutePath());
        }
    
        final File file = new File(dataFolder, "config.yml");
        configuration = new NetworkWatchdogConfiguration();
        
        if (!file.isFile()) {
            try {
                Resources.dump(getClassLoader(), "config.yml", file);
            } catch (IOException e) {
                Exceptions.report(getServer().getConsoleSender(), e, "Saving default configuration to file: " + file.getAbsolutePath());
            }
        }
    
        final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        configuration.setDebug(yaml.getBoolean("debug"));
        configuration.setEnable(yaml.getBoolean("enable"));
        configuration.setInterval(yaml.getLong("interval"));
        configuration.setThreshold(yaml.getInt("threshold"));
        configuration.setUrls(yaml.getStringList("urls").stream()
            .map(url -> {
                try {
                    return new URL(url);
                } catch (MalformedURLException e) {
                    Exceptions.report(getServer().getConsoleSender(), e, "Construct URL: " + url);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()));
        configuration.setIntroduce(yaml.getBoolean("introduce"));
    }
    
    public void loadMessages() {
        final File dataFolder = getDataFolder();
        if (!dataFolder.isDirectory() && !dataFolder.mkdirs()) {
            throw new IllegalStateException("Can not create data folder: " + dataFolder.getAbsolutePath());
        }
    
        final File file = new File(dataFolder, "messages.yml");
        if (!file.isFile()) {
            try {
                Resources.dump(getClassLoader(), "messages.yml", file);
            } catch (IOException e) {
                Exceptions.report(getServer().getConsoleSender(), e, "Saving default messages to file: " + file.getAbsolutePath());
            }
        }
    
        messages = new NetworkWatchdogMessages(YamlConfiguration.loadConfiguration(file));
    }
    
    @Override
    public void onDisable() {
        instance = null;
    }
}
