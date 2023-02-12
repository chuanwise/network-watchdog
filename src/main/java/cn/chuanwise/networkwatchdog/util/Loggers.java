package cn.chuanwise.networkwatchdog.util;

import cn.chuanwise.networkwatchdog.NetworkWatchdogPlugin;
import org.bukkit.command.CommandSender;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Loggers
 *
 * @author Chuanwise
 */
public class Loggers {
    private Loggers() {
    }
    
    private static final String PLUGIN_NAME = "NetworkWatchdog";
    private static final String INFO_PREFIX = "§7[§b" + PLUGIN_NAME + "§7] §f";
    private static final String WARNING_PREFIX = "§7[§6" + PLUGIN_NAME + " §7| §eWARN§7] §e";
    private static final String ERROR_PREFIX = "§7[§4" + PLUGIN_NAME + " §7| §cERROR§7] §c";
    private static final String DEBUG_PREFIX = "§7[§1" + PLUGIN_NAME + " §7| §1DEBUG§7] §1";
    private static final String SUCCESS_PREFIX = "§7[§2" + PLUGIN_NAME + " §7| §aSUCCESS§7] §a";
    
    public static void error(String message) {
        sendError(NetworkWatchdogPlugin.getInstance().getServer().getConsoleSender(), message);
    }
    
    public static void sendError(CommandSender commandSender, String message) {
        Objects.requireNonNull(commandSender, "Command sender is null!");
        Objects.requireNonNull(message, "Message is null!");
    
        commandSender.sendMessage(ERROR_PREFIX + message);
    }
    
    public static void info(String message) {
        sendInfo(NetworkWatchdogPlugin.getInstance().getServer().getConsoleSender(), message);
    }
    
    public static void sendInfo(CommandSender commandSender, String message) {
        Objects.requireNonNull(commandSender, "Command sender is null!");
        Objects.requireNonNull(message, "Message is null!");
    
        commandSender.sendMessage(INFO_PREFIX + message);
    }
    
    public static void warning(String message) {
        sendWarning(NetworkWatchdogPlugin.getInstance().getServer().getConsoleSender(), message);
    }
    
    public static void sendWarning(CommandSender commandSender, String message) {
        Objects.requireNonNull(commandSender, "Command sender is null!");
        Objects.requireNonNull(message, "Message is null!");
    
        commandSender.sendMessage(WARNING_PREFIX + message);
    }
    
    public static void success(String message) {
        sendSuccess(NetworkWatchdogPlugin.getInstance().getServer().getConsoleSender(), message);
    }
    
    public static void sendSuccess(CommandSender commandSender, String message) {
        Objects.requireNonNull(commandSender, "Command sender is null!");
        Objects.requireNonNull(message, "Message is null!");
    
        commandSender.sendMessage(SUCCESS_PREFIX + message);
    }
    
    public static void debug(String message) {
        sendDebug(NetworkWatchdogPlugin.getInstance().getServer().getConsoleSender(), message);
    }
    
    public static void sendDebug(CommandSender commandSender, String message) {
        Objects.requireNonNull(commandSender, "Command sender is null!");
        Objects.requireNonNull(message, "Message is null!");
    
        if (NetworkWatchdogPlugin.getInstance()
            .getConfiguration()
            .isDebug()) {
            commandSender.sendMessage(DEBUG_PREFIX + message);
        }
    }
    
    public static void debug(Supplier<String> supplier) {
        sendDebug(NetworkWatchdogPlugin.getInstance().getServer().getConsoleSender(), supplier);
    }
    
    public static void sendDebug(CommandSender commandSender, Supplier<String> supplier) {
        Objects.requireNonNull(commandSender, "Command sender is null!");
        Objects.requireNonNull(supplier, "Message supplier is null!");
    
        if (NetworkWatchdogPlugin.getInstance()
            .getConfiguration()
            .isDebug()) {
            commandSender.sendMessage(DEBUG_PREFIX + supplier.get());
        }
    }
}