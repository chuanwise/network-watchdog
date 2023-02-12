package cn.chuanwise.networkwatchdog.util;

import java.io.*;
import java.util.Objects;

/**
 * This class consists of static utility methods for operating on resources.
 *
 * @author Chuanwise
 */
public class Resources {
    private Resources() {
    }
    
    /**
     * Copy resource file
     *
     * @param classLoader class loader
     * @param path        resource path
     * @param destination destination file
     * @throws IOException exception occurred in dumping
     */
    public static void dump(ClassLoader classLoader, String path, File destination) throws IOException {
        Objects.requireNonNull(classLoader, "Class loader is null!");
        Objects.requireNonNull(path, "Path is null!");
        Objects.requireNonNull(destination, "Destination is null!");
        
        if (!destination.isFile() && !destination.createNewFile()) {
            throw new IllegalStateException("Can not create new file: " + destination.getAbsolutePath());
        }
        
        try (OutputStream outputStream = new FileOutputStream(destination);
             InputStream inputStream = classLoader.getResourceAsStream(path)) {
    
            Objects.requireNonNull(inputStream, "Resource file '" + path + "' doesn't exist!");
    
            final byte[] buffer = new byte[1024];
            int count;
    
            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
        }
    }
}
