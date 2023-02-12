package cn.chuanwise.networkwatchdog;

import cn.chuanwise.networkwatchdog.util.Loggers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
        
        if (arguments.size() != 1) {
            displayUsage(sender);
            return true;
        }
    
        if ("reload".equals(arguments.get(0))) {
            NetworkWatchdogPlugin.getInstance().loadMessages();
            NetworkWatchdogPlugin.getInstance().loadConfiguration();
            Loggers.sendInfo(sender, "已尝试重新载入配置文件");
        } else {
            displayUsage(sender);
        }
        return true;
    }
    
    private void displayUsage(CommandSender sender) {
        Loggers.sendInfo(sender, "§7/§3nw reload§7 - §f重新载入插件配置文件");
    }
}
