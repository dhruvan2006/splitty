package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {
    private Group group;
    private Participant participant1;
    private Participant participant2;
    private Participant groupLeader;

    @BeforeEach
    void setUp() {
        group = new Group("Test Group");
        participant1 = new Participant("John", "Doe");
        participant2 = new Participant("Jane", "Doe");
        groupLeader = new Participant("Group", "Leader");
    }

    @Test
    void addToGroup() {
        group.addToGroup(participant1);
        assertTrue(group.getParticipants().contains(participant1));
    }

    @Test
    void removeFromGroup() {
        group.setGroupLeader(groupLeader);
        group.addToGroup(groupLeader);
        group.addToGroup(participant1);
        group.addToGroup(participant2);
        
        // Trying to remove participant1 without being the group leader
        Participant nonLeader = new Participant("Non", "Leader");
        group.removeFromGroup(nonLeader, participant1);
        assertTrue(group.getParticipants().contains(participant1)); // participant1 should not be removed
        
        // Removing participant1 by the group leader
        group.removeFromGroup(groupLeader, participant1);
        assertFalse(group.getParticipants().contains(participant1));
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
        
        group.setGroupLeader(groupLeader);
        assertEquals(groupLeader, group.getGroupLeader());
    }

    @Test
    void setGroupLeader() {
        group.setGroupLeader(groupLeader);
        assertEquals(groupLeader, group.getGroupLeader());
    }
}
