package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {
    private Group group;
    private Participant p1;
    private Participant p2;
    private Participant groupLeader;

    @BeforeEach
    void setUp() {
        group = new Group("Test Group");
        p1 = new Participant("John", "Doe", "s", "d", "JD");
        p2 = new Participant("Jane", "Doe", "s", "l", "JD");
        groupLeader = new Participant("Group", "Leader", "w", "aa", "GL");
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

        // Trying to remove Participant 1 without being the group leader
        Participant nonLeader = new Participant("Non", "Leader", "l", "x", "NL");

        group.removeFromGroup(nonLeader, p1);
        assertTrue(group.getParticipants().contains(p1)); // Participant 1 should not be removed

        // Removing Participant 1 by the group leader
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

    @Test
    public void testGetId() {
        assertEquals(0,group.getId());
    }

    @Test
    public void testSetId() {
        group.setId(7)
        assertEquals(7,group.getId());
    }

}
