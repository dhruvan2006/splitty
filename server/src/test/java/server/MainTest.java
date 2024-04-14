package server;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testPasswordLength() {
        String password = Main.passwordGenerator();
        assertEquals(15, password.length());
    }

    @Test
    void testPasswordComposition() {
        String password = Main.passwordGenerator();
        assertTrue(Pattern.matches("[A-Za-z0-9!@#$%^&*()_+]{15}", password));
    }
}