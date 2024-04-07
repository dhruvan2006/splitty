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
    void individualDebtTest()
    {
        Event event = new Event("z");
        Participant a = new Participant("a","a","a");
        Participant b = new Participant("b","b","b");
        Participant c = new Participant("c","c","c");
        Participant d = new Participant("d","d","d");
        Participant e = new Participant("e","e","e");
        event.addParticipant(a);
        event.addParticipant(b);
        event.addParticipant(c);
        event.addParticipant(d);
        event.addParticipant(e);
        Expense expense = new Expense("TEST",20,e,event);
        Expense expense2 = new Expense("TEST",16,a,event);
        event.addExpense(expense);
        event.addExpense(expense2);

        Map<Participant,Map<Participant,Integer>> result = event.calculateIndividualDebt();
        
        assertEquals(4, result.get(a).get(e));
        assertNull(result.get(a).get(b));
        assertEquals(3, result.get(b).get(a));
        assertEquals(4, result.get(b).get(e));
        assertEquals(3, result.get(e).get(a));
    }
}
