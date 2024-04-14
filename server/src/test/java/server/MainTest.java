package server;

import org.junit.jupiter.api.Test;
import server.Main;
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

    @Test
    void testUniquePasswords() {
        String password1 = Main.passwordGenerator();
        String password2 = Main.passwordGenerator();
        assertNotEquals(password1, password2);
    }

    @Test
    void testGetPassword() {
        String generatedPassword = Main.passwordGenerator();
        assertEquals(generatedPassword, Main.getPassword());
    }

    @Test
    void testSetPassword() {
        String newPassword = "NewSecurePassword123!";
        Main.setPassword(newPassword);
        assertEquals(newPassword, Main.getPassword());
    }

    @Test
    void testPasswordStrong() {
        String strongPassword = "StrongPassword123!@#";
        assertTrue(Main.isPasswordStrong(strongPassword));
    }

    @Test
    void testPasswordNotStrong() {
        String weakPassword = "weak";
        assertFalse(Main.isPasswordStrong(weakPassword));
    }

}