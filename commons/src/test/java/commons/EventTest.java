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
        Participant p = new Participant("Z", "X", "ZX");
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
}
