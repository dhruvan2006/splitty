package commons;

import org.junit.jupiter.api.Test;

import java.util.*;

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
        Participant p = new Participant("Z", "X", "L");
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

        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());
        e.setExpenses(expenses);

        assertEquals(expenses, e.getExpenses());
    }

    @Test
    void addParticipant() {
        Event e = new Event("z");
        assertTrue(e.getParticipants().isEmpty());

        Participant p = new Participant();
        e.addParticipant(p);

        List<Participant> participants = new ArrayList<>();
        participants.add(p);

        assertEquals(participants, e.getParticipants());
    }

    @Test
    void addExpense() {
        Event e = new Event("x");
        assertTrue(e.getExpenses().isEmpty());

        Expense expense = new Expense();
        e.addExpense(expense);

        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);

        assertEquals(expenses, e.getExpenses());
    }

    @Test
    void toStringTest(){
        Event e = new Event("x");
        assertTrue(e.toString().contains("x (code: "));
    }

    @Test
    void testUpdateParticipant() {
        Event e = new Event();
        e.setParticipants(new ArrayList<>());
        Participant p1 = new Participant();
        p1.setId(1L);
        e.addParticipant(p1);
        Participant p2 = new Participant();
        p2.setUserName("Hello");
        assertTrue(e.updateParticipant(1L, p2));
        assertEquals("Hello", e.getParticipants().get(0).getUserName());
    }

    @Test
    void testUpdateParticipantFailure() {
        Event e = new Event();
        e.setParticipants(new ArrayList<>());
        Participant p1 = new Participant();
        p1.setUserName("Hello2");
        assertFalse(e.updateParticipant(3, p1));
    }

    @Test
    void testCalculateIndividualDebt() {
        Event e = new Event();
        e.setId(1L);
        Participant p1 = new Participant();
        p1.setId(1L);
        Participant p2 = new Participant();
        p2.setId(2L);
        e.setParticipants(new ArrayList<>());
        e.addParticipant(p1);
        e.addParticipant(p2);

        e.setExpenses(new ArrayList<>());
        e.getExpenses().add(new Expense("Title", 100, p1, e));

        Map<Participant, Map<Participant, Integer>> debts = e.calculateIndividualDebt();

        assertNotNull(debts.get(p2));
        assertEquals(50, (int) debts.get(p2).get(p1));
        assertNull(debts.get(p1).get(p2));
    }

    @Test
    void testCalculateOptimizedDebts1() {
        Event e = new Event();
        e.setId(1L);
        Participant p1 = new Participant();
        p1.setId(1L);
        Participant p2 = new Participant();
        p2.setId(2L);
        Participant p3 = new Participant();
        p3.setId(3L);
        e.setParticipants(new ArrayList<>());
        e.addParticipant(p1);
        e.addParticipant(p2);
        e.addParticipant(p3);

        e.setExpenses(new ArrayList<>());
        e.getExpenses().add(new Expense("Title", 100, p1, e));

        Map<Participant, Map<Participant, Integer>> debts = e.calculateOptimizedDebts();

        assertNotNull(debts.get(p2));
        assertEquals(33, (int) debts.get(p2).get(p1));
        assertEquals(33, (int) debts.get(p3).get(p1));
    }

    @Test
    void testCalculateOptimizedDebts2() {
        Event e = new Event();
        e.setId(1L);
        Participant p1 = new Participant();
        p1.setId(1L);
        Participant p2 = new Participant();
        p2.setId(2L);
        Participant p3 = new Participant();
        p3.setId(3L);
        e.setParticipants(new ArrayList<>());
        e.addParticipant(p1);
        e.addParticipant(p2);
        e.addParticipant(p3);

        e.setExpenses(new ArrayList<>());
        e.getExpenses().add(new Expense("Title", 300, p1, e));
        e.getExpenses().add(new Expense("Title", 150, p2, e));

        Map<Participant, Map<Participant, Integer>> debts = e.calculateOptimizedDebts();

        assertTrue(debts.get(p1).isEmpty());

        assertEquals(50, (int) debts.get(p2).get(p1));

        assertEquals(50, (int) debts.get(p3).get(p2));
        assertEquals(100, (int) debts.get(p3).get(p1));
    }

    @Test
    void testEquals() {
        Event e1 = new Event();
        e1.setId(1L);

        Event e2 = new Event();
        e2.setId(1L);

        assertEquals(e1, e2);
    }
}
