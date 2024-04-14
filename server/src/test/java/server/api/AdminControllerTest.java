package server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    @Test
    void testChangePassword() {
        AdminController contr = new AdminController();
        assertTrue(contr.changePassword("password"));
        assertEquals("password",Main.getPassword());
    }

    @Test
    void testChangePasswordInvalid() {
        AdminController contr = new AdminController();
        assertFalse(contr.changePassword(null));
    }

    @Test
    void testAuth() {
        AdminController contr = new AdminController();
        contr.changePassword("password");
        assertTrue(contr.authenticate("password"));
    }

    @Test
    void testAuthRejected() {
        AdminController contr = new AdminController();
        contr.changePassword("password");
        assertFalse(contr.authenticate("random"));
    }


   
}
