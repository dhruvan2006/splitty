package commons;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
public class ParticipantTest {
    @Test
    public void testConstructorAndGetters() {
        String firstName = "John";
        String lastName = "Doe";

        Participant participant = new Participant(firstName, lastName, "x", "s");

        assertEquals(firstName, participant.getFirstName());
        assertEquals(lastName, participant.getLastName());
    }

    @Test
    public void testSetters() {
        Participant participant = new Participant("John", "Doe", "x", "f");

        participant.setFirstName("Jane");
        participant.setLastName("Smith");

        assertEquals("Jane", participant.getFirstName());
        assertEquals("Smith", participant.getLastName());
    }

    @Test
    public void testGetId() {
        Participant participant = new Participant("John", "Doe", "x", "f");

        assertEquals(0, participant.getId());
    }
    @Test
    public void checkGetSetId(){
        var p = new Participant("f","l", "r", "s");
        p.setId(2020200202);
        assertEquals(2020200202, p.getId());
    }
    @Test
    public void checkGetSetFullName(){
        var p = new Participant("f","l", "r", "s");
        assertEquals("f",p.getFirstName());
        p.setFirstName("x");
        assertEquals("x",p.getFirstName());
    }
    @Test
    public void checkGetSetLastName(){
        var p = new Participant("f","l", "r", "s");
        assertEquals("l",p.getLastName());
        p.setLastName("x");
        assertEquals("x",p.getLastName());
    }


    @Test
    public void testEquals() {
        var p1 = new Participant("f","l", "r", "s");
        var p2 = new Participant("f","l", "r", "s");

        assertEquals(p1, p2);
    }

}
