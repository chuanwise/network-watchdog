package cn.chuanwise.networkwatchdog;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * Event represents network disconnected
 *
 * @author Chuanwise
 */
public class NetworkDisconnectedEvent
    extends Event
    implements Cancellable {
    
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final List<URL> testedURLs;
    private volatile boolean cancelled;
    
    public NetworkDisconnectedEvent(List<URL> testedURLs) {
        Objects.requireNonNull(testedURLs, "Tested URLs are null!");
        
        this.testedURLs = testedURLs;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    
    public List<URL> getTestedURLs() {
        return testedURLs;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
