package commons;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
public class ParticipantTest {
    @Test
    public void testConstructorAndGetters() {
        String firstName = "John";
        String lastName = "Doe";

        Participant participant = new Participant(firstName, lastName);

        assertEquals(firstName, participant.getFirstName());
        assertEquals(lastName, participant.getLastName());
    }

    @Test
    public void testSetters() {
        Participant participant = new Participant("John", "Doe");

        participant.setFirstName("Jane");
        participant.setLastName("Smith");

        assertEquals("Jane", participant.getFirstName());
        assertEquals("Smith", participant.getLastName());
    }

    @Test
    public void testGetId() {
        Participant participant = new Participant("John", "Doe");

        assertEquals(0, participant.getId());
    }

    @Test
    public void testEquals() {
        Participant participant1 = new Participant("John", "Doe");
        Participant participant2 = new Participant("John", "Doe");

        assertEquals(participant1, participant2);
    }

    @Test
    public void testHashCode() {
        Participant participant1 = new Participant("John", "Doe");
        Participant participant2 = new Participant("John", "Doe");

        assertEquals(participant1.hashCode(), participant2.hashCode());
    }

    @Test
    public void testToString() {
        Participant participant = new Participant("John", "Doe");
        String expectedToString = "Participant{id=0, firstName='John', lastName='Doe'}";

        assertEquals(expectedToString, participant.toString());
    }
}
