package cn.chuanwise.networkwatchdog;

import cn.chuanwise.networkwatchdog.util.Exceptions;
import cn.chuanwise.networkwatchdog.util.Loggers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NetworkWatchdogCommand
    implements CommandExecutor {
    
    private static final NetworkWatchdogCommand INSTANCE = new NetworkWatchdogCommand();
    
    private NetworkWatchdogCommand() {
    }
    
    public static NetworkWatchdogCommand getInstance() {
        return INSTANCE;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final List<String> arguments = Arrays.stream(args)
            .filter(Objects::nonNull)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
        
        if (arguments.size() != 1 || !arguments.get(0).equals("reload")) {
            displayUsage(sender);
            return true;
        }
        
        final String permission = "network-watchdog.command";
        if (sender.hasPermission(permission)) {
            boolean success = true;
            try {
                NetworkWatchdogPlugin.getInstance().loadMessages();
            } catch (IOException e) {
                Exceptions.report(sender, e, "Loading messages");
                success = false;
            }
            try {
                NetworkWatchdogPlugin.getInstance().loadConfiguration();
            } catch (IOException e) {
                Exceptions.report(sender, e, "Loading configuration");
                success = false;
            }
            if (success) {
                Loggers.sendInfo(sender, "Configuration and messages reloaded!");
            }
        } else {
            Loggers.sendError(sender, "Require permission: " + permission);
        }
        return true;
    }
    
    private void displayUsage(CommandSender sender) {
        Loggers.sendInfo(sender, "Command Usages: \n§7- /§3nw reload§7 - §fReload configuration and messages.");
    }
}
