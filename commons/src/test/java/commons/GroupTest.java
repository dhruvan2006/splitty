package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {
    private Groups group;
    private Participant p1;
    private Participant p2;
    private Participant groupLeader;

    @BeforeEach
    void setUp() {
        group = new Groups("Test Group");
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
        group.setId(7);
        assertEquals(7,group.getId());
    }

    @Test
    public void toStringTest(){
        Participant formal = new Participant("Jane", "Doe", "s", "l", "JD","female");
        formal.setFormal(true);
        group.addToGroup(groupLeader);
        group.addToGroup(formal);
        assertTrue(group.toString().contains(group.getGroupName() + " ,ID: " + group.getId()));
        assertTrue(group.toString().contains("Group Leader, known as GL"));
        assertTrue(group.toString().contains("Ms. Jane Doe, known as JD"));
    }

    @Test
    void addAndGetExpenses() {
        Expense e = new Expense();
        Expense f = new Expense();
        group.addExpense(e);
        group.addExpense(f);

        List<Expense> result = new ArrayList<>();
        result.add(e);
        result.add(f);

        assertEquals(group.getExpenses(),result);

    }

    @Test
    void setExpenses() {
        Expense e = new Expense();
        Expense f = new Expense();
        List<Expense> settingList = new ArrayList<>();
        settingList.add(e);
        settingList.add(f);
        
        group.setExpenses(settingList);
        
        List<Expense> result = new ArrayList<>();
        result.add(e);
        result.add(f);

        assertEquals(group.getExpenses(),result);

    }






}
