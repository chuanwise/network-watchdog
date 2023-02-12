package cn.chuanwise.networkwatchdog.util;

import cn.chuanwise.networkwatchdog.NetworkWatchdogPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * This class consists of static utility methods for operating on exceptions.
 *
 * @author Chuanwise
 */
public class Exceptions {
    private Exceptions() {
    }
    
    /**
     * Report an exception to user
     *
     * @param sender user
     * @param cause  exception
     */
    public static void report(CommandSender sender, Throwable cause) {
        Objects.requireNonNull(sender, "Sender is null!");
        Objects.requireNonNull(cause, "Cause is null!");
    
        final String stackTrace = getStackTrace(cause);
        
        if (sender.hasPermission(Permissions.DISPLAY_EXCEPTION_STACK_TRACE)) {
            if (sender instanceof ConsoleCommandSender) {
                Loggers.sendError(sender, "Exception occurred: \n" + stackTrace);
            } else {
                final boolean debug = NetworkWatchdogPlugin.getInstance()
                    .getConfiguration()
                    .isDebug();
                if (debug) {
                    Loggers.sendError(sender, "Exception occurred: \n" + stackTrace);
                } else {
                    Loggers.sendError(sender, "Exception occurred: " + cause + ", stack traces printed in the console.");
                }
                Loggers.error(stackTrace);
            }
        } else {
            Loggers.sendError(sender, "Exception occurred, contact admins, please.");
        }
    }
    
    /**
     * Report an exception to user
     *
     * @param sender      user
     * @param cause       exception
     * @param description description for actions
     */
    public static void report(CommandSender sender, Throwable cause, String description) {
        Objects.requireNonNull(sender, "Sender is null!");
        Objects.requireNonNull(cause, "Cause is null!");
    
        final String stackTrace = getStackTrace(cause);
    
        if (sender.hasPermission(Permissions.DISPLAY_EXCEPTION_STACK_TRACE)) {
            if (sender instanceof ConsoleCommandSender) {
                Loggers.sendError(sender, "Exception occurred when " + description + ": \n" + stackTrace);
            } else {
                final boolean debug = NetworkWatchdogPlugin.getInstance()
                    .getConfiguration()
                    .isDebug();
                if (debug) {
                    Loggers.sendError(sender, "Exception occurred when " + description + ": \n" + stackTrace);
                } else {
                    Loggers.sendError(sender, "Exception occurred when " + description + ": " + cause + ", stack traces printed in the console.");
                }
                Loggers.error(stackTrace);
            }
        } else {
            Loggers.sendError(sender, "Exception occurred when " + description + ", contact admins, please.");
        }
    }
    
    /**
     * Print exception stack traces to string
     *
     * @param cause exception
     * @return stack traces
     */
    public static String getStackTrace(Throwable cause) {
        Objects.requireNonNull(cause, "Cause is null!");
    
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        cause.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}