package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {
    private commons.Group group;
    private commons.Participant  p1;
    private commons.Participant  p2;
    private commons.Participant  groupLeader;

    @BeforeEach
    void setUp() {
        group = new commons.Group("Test Group");
        p1 = new commons.Participant("John", "Doe");
        p2 = new commons.Participant("Jane", "Doe");
        groupLeader = new commons.Participant ("Group", "Leader");
    }

    @Test
    void addToGroup() {
        group.addToGroup(p1);
        assertTrue(group.getParticipants().contains(p1));
    }

    @Test
    void addToGroup2() {
        group.addToGroup(groupLeader);
        assertTrue(group.getParticipants().contains(groupLeader));
    }

    @Test
    void removeFromGroup() {
        group.addToGroup(groupLeader);
        group.setGroupLeader(groupLeader);
        group.addToGroup(p1);
        group.addToGroup(p2);

        // Trying to remove commons.Participant 1 without being the group leader
        commons.Participant  nonLeader = new commons.Participant ("Non", "Leader");
        group.removeFromGroup(nonLeader,p1);
        assertTrue(group.getParticipants().contains(p1)); // commons.Participant 1 should not be removed

        // Removing commons.Participant 1 by the group leader
        group.removeFromGroup(groupLeader, p1);
        assertFalse(group.getParticipants().contains(p1));
    }

    @Test
    void getGroupName() {
        assertEquals("Test Group", group.getGroupName());
    }

    @Test
    void setGroupName() {
        group.setGroupName("New Group");
        assertEquals("New Group", group.getGroupName());
    }

    @Test
    void isPinned() {
        assertFalse(group.isPinned());
    }

    @Test
    void setPinned() {
        group.setPinned(true);
        assertTrue(group.isPinned());
    }

    @Test
    void getGroupLeader() {
        assertNull(group.getGroupLeader());

        group.addToGroup(groupLeader);
        group.setGroupLeader(groupLeader);
        assertEquals(groupLeader, group.getGroupLeader());
    }

    @Test
    void setGroupLeader() {
        group.addToGroup(groupLeader);
        group.setGroupLeader(groupLeader);
        assertEquals(groupLeader, group.getGroupLeader());
    }
}
