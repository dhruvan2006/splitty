package client.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final Properties configProperties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try (InputStream inputStream = Configuration.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                configProperties.load(inputStream);
            } else {
                throw new RuntimeException("Could not find the configuration file: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading the configuration file: " + CONFIG_FILE, e);
        }
    }

    public static String getServerUrl() {
        return configProperties.getProperty("server.url");
    }
}