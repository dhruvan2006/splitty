package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {
    private Groups group;
    private Participant p1;
    private Participant p2;
    private Participant groupLeader;

    @BeforeEach
    void setUp() {
        group = new Groups("Test Group");
        p1 = new Participant("John", "Doe", "JD");
        p2 = new Participant("Jane", "Doe", "JD");
        groupLeader = new Participant("Group", "Leader", "GL");
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
        assertEquals(0, group.getId());
    }

    @Test
    public void testSetId() {
        group.setId(7);
        assertEquals(7, group.getId());
    }

    @Test
    public void toStringTest() {
        Participant formal = new Participant("Jane", "Doe", "JD");
        group.addToGroup(groupLeader);
        group.addToGroup(formal);
        assertTrue(group.toString().contains(group.getGroupName() + " ,ID: " + group.getId()));
    }

}
