package client.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    @BeforeEach
    void setUp() {
        Configuration.reset();
    }

    @Test
    void testGetServerUrlFromTestPropertiesTrue() {
        Properties testProperties = new Properties();
        testProperties.setProperty("server.url", "https://example.com:6969/");
        Configuration configuration = Configuration.getInstance(testProperties);
        assertEquals("https://example.com:6969/", configuration.getServerUrl());
    }

    @Test
    void testGetServerUrlFromTestPropertiesFalse() {
        Properties testProperties = new Properties();
        testProperties.setProperty("server.url", "localhost:4200");
        Configuration configuration = Configuration.getInstance(testProperties);
        assertNotEquals("eduard", configuration.getServerUrl());
    }

    @Test
    void testExceptionThrowing() {
        assertThrows(RuntimeException.class, () -> {
        final Configuration configuration = Configuration.getInstance();
        });
    }

    @Test
    void testEquals() {
        Properties testProperties = new Properties();
        testProperties.setProperty("server.url", "https://example.com:6969/");
        Configuration configuration1 = Configuration.getInstance(testProperties);
        Configuration configuration2 = Configuration.getInstance(testProperties);
        assertEquals(configuration1, configuration2);
    }

    @Test
    void testHashCode() {
        Properties testProperties = new Properties();
        testProperties.setProperty("server.url", "https://example.com:6969/");
        Configuration configuration = Configuration.getInstance(testProperties);

        Properties testProperties2 = new Properties();
        testProperties2.setProperty("server.url", "https://example.com:6969/");
        Configuration configuration2 = Configuration.getInstance(testProperties2);

        assertEquals(configuration.hashCode(), configuration2.hashCode());
        
    }

}