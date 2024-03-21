package commons;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
public class ParticipantTest {
    @Test
    public void testConstructorAndGetters() {
        String email = "john@example.com";
        String IBAN = "IBAN12345";
        String userName = "JD";

        Participant participant = new Participant(email, IBAN, userName);


        assertEquals(email, participant.getEmail());
        assertEquals(IBAN, participant.getIBAN());
        assertEquals(userName, participant.getUserName());
    }

    @Test
    public void testSetters() {

        Participant participant = new Participant("john@example.com", "IBAN12345", "JD");

        participant.setEmail("jane@example.com");
        participant.setIBAN("IBAN67890");
        participant.setUserName("JS");

        assertEquals("jane@example.com", participant.getEmail());
        assertEquals("IBAN67890", participant.getIBAN());
        assertEquals("JS", participant.getUserName());
    }

    @Test
    public void testGetId() {
        Participant participant = new Participant("john@example.com", "IBAN12345", "JD");

        assertEquals(0, participant.getId());
    }

    @Test
    public void checkGetSetId(){
        Participant p = new Participant("john@example.com", "IBAN12345", "JD");
        p.setId(2020200202);
        assertEquals(2020200202, p.getId());
    }

    @Test
    public void checkGetSetFullName(){
        Participant p = new Participant("john@example.com", "IBAN12345", "JD");
        assertEquals("john@example.com",p.getEmail());
        p.setEmail("jane@example.com");
        assertEquals("jane@example.com",p.getEmail());
    }

    @Test
    public void checkGetSetIBAN(){
        Participant p = new Participant("john@example.com", "IBAN12345", "JD");
        assertEquals("IBAN12345",p.getIBAN());
        p.setIBAN("IBAN67890");
        assertEquals("IBAN67890",p.getIBAN());
    }


    @Test
    public void testEquals() {

        Participant p2 = new Participant("john@example.com", "IBAN12345", "JD");
        Participant p1 = new Participant("john@example.com", "IBAN12345", "JD");
        assertEquals(p1, p2);
    }


}
