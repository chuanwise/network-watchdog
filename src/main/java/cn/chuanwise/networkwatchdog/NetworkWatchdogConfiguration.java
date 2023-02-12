package cn.chuanwise.networkwatchdog;

import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Configuration for NetworkWatchdog
 *
 * @author Chuanwise
 */
public class NetworkWatchdogConfiguration {
    
    /**
     * Is function enable.
     */
    private boolean enable = true;
    
    /**
     * Is debug logs printed.
     */
    private boolean debug = false;
    
    /**
     * Testing URLs.
     */
    private Set<URL> urls = new HashSet<>();
    
    /**
     * Send self introduction to all HTTP URLs,
     * message key is self-introduction.
     */
    private boolean introduce = false;
    
    /**
     * How many tick(s) should check if network is connected.
     */
    private int interval = 20;
    
    /**
     * Shut down the server after checking how many
     * continuous times for checking network.
     */
    private int threshold = 5;
    
    public boolean isEnable() {
        return enable;
    }
    
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    
    public boolean isDebug() {
        return debug;
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public Set<URL> getUrls() {
        return urls;
    }
    
    public void setUrls(Set<URL> urls) {
        Objects.requireNonNull(urls, "URLs are null!");
        this.urls = urls;
    }
    
    public int getInterval() {
        return interval;
    }
    
    public void setInterval(int interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("Interval should be greater than 0!");
        }
        this.interval = interval;
    }
    
    public int getThreshold() {
        return threshold;
    }
    
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
    
    public boolean isIntroduce() {
        return introduce;
    }
    
    public void setIntroduce(boolean introduce) {
        this.introduce = introduce;
    }
}
