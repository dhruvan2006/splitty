package commons;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
public class ParticipantTest {
    @Test
    public void testConstructorAndGetters() {
        String firstName = "John";
        String lastName = "Doe";
        String userName = "JD";

        Participant participant = new Participant(firstName, lastName, userName);

        assertEquals(firstName, participant.getFirstName());
        assertEquals(lastName, participant.getLastName());
        assertEquals(userName, participant.getUserName());
    }

    @Test
    public void testSetters() {
        Participant participant = new Participant("John", "Doe", "JD");

        participant.setFirstName("Jane");
        participant.setLastName("Smith");
        participant.setUserName("JS");

        assertEquals("Jane", participant.getFirstName());
        assertEquals("Smith", participant.getLastName());
        assertEquals("JS", participant.getUserName());
    }

    @Test
    public void testGetId() {
        Participant participant = new Participant("John", "Doe", "JD");

        assertEquals(0, participant.getId());
    }
    @Test
    public void checkGetSetId(){
        var p = new Participant("f","l", "fl");
        p.setId(2020200202);
        assertEquals(2020200202, p.getId());
    }
    @Test
    public void checkGetSetFullName(){
        var p = new Participant("f","l", "fl");
        assertEquals("f",p.getFirstName());
        p.setFirstName("x");
        assertEquals("x",p.getFirstName());
    }
    @Test
    public void checkGetSetLastName(){
        var p = new Participant("f","l", "fl");
        assertEquals("l",p.getLastName());
        p.setLastName("x");
        assertEquals("x",p.getLastName());
    }


    @Test
    public void testEquals() {
        Participant participant1 = new Participant("John", "Doe", "JD");
        Participant participant2 = new Participant("John", "Doe", "JD");

        assertEquals(participant1, participant2);
    }

}
