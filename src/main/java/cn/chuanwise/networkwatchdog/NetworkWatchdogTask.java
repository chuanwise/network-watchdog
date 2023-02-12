package cn.chuanwise.networkwatchdog;

import cn.chuanwise.networkwatchdog.util.Exceptions;
import cn.chuanwise.networkwatchdog.util.Loggers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Task for detecting network status and shutdown server.
 *
 * @author Chuanwise
 */
public class NetworkWatchdogTask
    implements Runnable {
    
    private static final NetworkWatchdogTask INSTANCE = new NetworkWatchdogTask();
    
    private NetworkWatchdogTask() {
    }
    
    public static NetworkWatchdogTask getInstance() {
        return INSTANCE;
    }
    
    /**
     * Random number generator for selecting URL randomly
     */
    private static final Random RANDOM = new Random();
    
    /**
     * Recent tested fail URLs
     */
    private final List<URL> testedURLs = new ArrayList<>();
    
    private int countdown = 0;
    
    @Override
    public void run() {
        final NetworkWatchdogConfiguration configuration = NetworkWatchdogPlugin.getInstance().getConfiguration();
        if (countdown >= configuration.getInterval()) {
            countdown = 0;
        } else {
            countdown++;
            return;
        }
    
        if (!configuration.isEnable()) {
            runTask(() -> Loggers.debug("Network watchdog task ran, but function disabled."));
            return;
        }
        
        // selecting url randomly
        final Set<URL> urls = configuration.getUrls();
        final Iterator<URL> iterator = urls.iterator();
        final int index = RANDOM.nextInt(urls.size());
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        final URL url = iterator.next();
        
        boolean reachable = false;
    
        // open connection
        try {
            final URLConnection urlConnection = url.openConnection();
    
            urlConnection.connect();
            reachable = true;
    
            if (configuration.isIntroduce()) {
                if (urlConnection instanceof HttpURLConnection) {
                    final HttpURLConnection connection = (HttpURLConnection) urlConnection;
                    final String introduction = NetworkWatchdogPlugin.getInstance()
                        .getMessages()
                        .format("self-introduction");
    
                    connection.setRequestProperty("User-Agent", introduction);
                    
                    final int responseCode = connection.getResponseCode();
                    final StringBuilder stringBuilder = new StringBuilder();
                    
                    // read message returns
                    if (responseCode == 200) {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) { // 循环从流中读取
                                stringBuilder.append(line).append("\n");
                            }
                        }
                        
                        final String message = stringBuilder.toString().trim();
                        if (message.length() == 0) {
                            runTask(() -> Loggers.debug("Test successfully, url is " + url +", no message"));
                        } else {
                            runTask(() -> Loggers.debug("Test successfully, url is " + url +", message: " + stringBuilder));
                        }
                    } else {
                        runTask(() -> Loggers.warning("Response code for URL " + url + " is " + responseCode + ", not 200!"));
                    }
                } else {
                    runTask(() -> Loggers.debug("Test successfully, url is " + url +", and it's not a http connection"));
                }
            } else {
                runTask(() -> Loggers.debug("Test successfully, url is " + url +", no introduction"));
            }
        } catch (IOException e) {
            runTask(() -> Loggers.warning("Fail to open connection for random selected URL: " + url + "\n" + Exceptions.getStackTrace(e)));
        }
    
        final int threshold = configuration.getThreshold();
        if (reachable) {
            testedURLs.clear();
            return;
        }
    
        testedURLs.add(url);
    
        final int failCount = testedURLs.size();
        if (failCount >= threshold) {
            testedURLs.clear();
            
            runTask(() -> {
                Loggers.error("Network testing fail count is " + failCount + ", greater than threshold " + threshold + ", event triggered!");
    
                final NetworkDisconnectedEvent event = new NetworkDisconnectedEvent(testedURLs);
                NetworkWatchdogPlugin.getInstance()
                    .getServer()
                    .getPluginManager()
                    .callEvent(event);
    
                if (event.isCancelled()) {
                    Loggers.warning("NetworkDisconnectedEvent cancelled");
                } else {
                    Loggers.error("Shutdown for network disconnected");
                    NetworkWatchdogPlugin.getInstance()
                        .getServer()
                        .shutdown();
                }
            });
        }
    }
    
    private void runTask(Runnable action) {
        NetworkWatchdogPlugin.getInstance()
            .getServer()
            .getScheduler()
            .runTask(NetworkWatchdogPlugin.getInstance(), action);
    }
}
