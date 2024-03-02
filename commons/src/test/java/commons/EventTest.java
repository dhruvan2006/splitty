package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class EventTest {

    @Test
    void testConstructor() {
        Event e = new Event("q");
        assertNotNull(e);
    }

    @Test
    void testGetID_testSetID() {
        Event e = new Event("w");
        e.setId(13);
        assertEquals(13, e.getId());
    }

    @Test
    void testGetInviteCode() {
        Event e = new Event("e");
        assertNotNull(e.getInviteCode());
    }

    @Test
    void testSetInviteCode() {
        Event e = new Event("r");
        e.setInviteCode("qwerty");
        assertEquals("qwerty", e.getInviteCode());
    }

    @Test
    void testGetTitle() {
        Event e = new Event("t");
        assertEquals("t", e.getTitle());
    }

    @Test
    void testSetTitle() {
        Event e = new Event("y");
        e.setTitle("q");
        assertEquals("q", e.getTitle());
    }

    @Test
    void testGetParticipants() {
        Event e = new Event("a");
        assertNotNull(e.getParticipants());
    }

    @Test
    void testSetParticipants() {
        Event e = new Event("s");

        List<Participant> participants = new ArrayList<>();
        Participant p = new Participant("Z", "X");
        participants.add(p);

        e.setParticipants(participants);

        assertEquals(participants, e.getParticipants());
    }

    @Test
    void testEqualsTrue() {
        Event e = new Event("d");
        assertEquals(e, e);
    }

    @Test
    void testEqualsFalse() {
        Event e = new Event("");
        Event e1 = new Event("f");
        assertNotEquals(e, e1);
        assertNotEquals(e1, e);
    }

    @Test
    void testHashCode() {
        Event e = new Event("");
        Event e1 = new Event("f");
        assertEquals(e.hashCode(), e.hashCode());
        assertNotEquals(e1.hashCode(), e.hashCode());
    }

    @Test
    void testToString() {
        Event e = new Event("Event");
        assertNotNull(e.toString());
    }

    @Test
    void testGetExpenses() {
        Event e = new Event("Event");
        assertTrue(e.getExpenses().isEmpty());
    }

    @Test
    void testSetExpenses() {
        Event e = new Event("Event");

        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant());
        e.setParticipants(participants);

        assertEquals(participants, e.getParticipants());
    }
}
