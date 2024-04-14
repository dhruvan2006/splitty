package client.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Configuration {

    private Properties configProperties;
    private static Configuration instance;

    private Configuration(Properties properties) {
        if (properties != null) {
            this.configProperties = properties;
        } else {
            this.configProperties = new Properties();
            loadDefaultProperties();
        }
    }

    private void loadDefaultProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                configProperties.load(inputStream);
            } else {
                throw new RuntimeException("Could not find the configuration file.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading the configuration file.", e);
        }
    }

    public static Configuration getInstance(Properties properties) {
        if (instance == null || properties != null) {
            instance = new Configuration(properties);
        }
        return instance;
    }

    public static Configuration getInstance() {
        return getInstance(null);
    }

    public String getServerUrl() {
        return configProperties.getProperty("server.url");
    }

    // For testing: Allow resetting the Configuration singleton
    public static void reset() {
        instance = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(configProperties, that.configProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configProperties);
    }
}