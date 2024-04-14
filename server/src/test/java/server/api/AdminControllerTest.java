package server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    @Test
    void testChangePassword() {
        AdminController contr = new AdminController();
        assertTrue(contr.changePasswordInMain("password"));
        assertEquals("password",Main.getPassword());
    }

    @Test
    void testChangePasswordInvalid() {
        AdminController contr = new AdminController();
        assertFalse(contr.changePasswordInMain(null));
    }


}
