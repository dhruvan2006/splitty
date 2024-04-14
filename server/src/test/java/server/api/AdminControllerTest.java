package server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import server.Main;

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
    void testAuth(){
        AdminController contr = new AdminController();
        contr.changePassword("password");
        assertEquals("password",Main.getPassword());
    }

    @Test 
    void testAuthInvalid(){
        AdminController contr = new AdminController();
        assertFalse(contr.changePassword(null));
    }




}
