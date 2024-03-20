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
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
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
    void toStringTest() {
        Event e = new Event("X");
        assertTrue(e.toString().contains("Event"));
        assertTrue(e.toString().contains("with the title: X, with the following participants:"));
        assertTrue(e.toString().contains("with the following expenses:"));
    }

    @Test
    void totalDebtTest()
    {
        Event event = new Event("z");
        Participant a = new Participant();
        Participant b = new Participant();
        Participant c = new Participant();
        Participant d = new Participant();
        Participant e = new Participant();
        event.addParticipant(a);
        event.addParticipant(b);
        event.addParticipant(c);
        event.addParticipant(d);
        event.addParticipant(e);
        Expense expense = new Expense("TEST",20,e);
        Expense expense2 = new Expense("TEST",16,a);
        event.addExpense(expense);
        event.addExpense(expense2);

        Map<Participant,Integer> result = event.calculateTotalDebt();

        assertEquals(result.get(a),5);
        assertEquals(result.get(b),9);
        assertEquals(result.get(e),4);
    }

    
    @Test
    void individualDebtTest()
    {
        Event event = new Event("z");
        Participant a = new Participant();
        Participant b = new Participant();
        Participant c = new Participant();
        Participant d = new Participant();
        Participant e = new Participant();
        event.addParticipant(a);
        event.addParticipant(b);
        event.addParticipant(c);
        event.addParticipant(d);
        event.addParticipant(e);
        Expense expense = new Expense("TEST",20,e);
        Expense expense2 = new Expense("TEST",16,a);
        event.addExpense(expense);
        event.addExpense(expense2);

        Map<Participant,Map<Participant,Integer>> result = event.calculateTotalDebt();
        
        assertEquals(result.get(a).get(e),5);
        assertEquals(result.get(a).get(b),0);
        assertEquals(result.get(b).get(a),4);
        assertEquals(result.get(b).get(e),5);
        assertEquals(result.get(e).get(a),4);
    }


    @Test
    void totalOwnedTest()
    {
        Event event = new Event("z");
        Participant a = new Participant();
        Participant b = new Participant();
        Participant c = new Participant();
        Participant d = new Participant();
        Participant e = new Participant();
        event.addParticipant(a);
        event.addParticipant(b);
        event.addParticipant(c);
        event.addParticipant(d);
        event.addParticipant(e);
        Expense expense = new Expense("TEST",20,e);
        Expense expense2 = new Expense("TEST",16,a);
        event.addExpense(expense);
        event.addExpense(expense2);

        Map<Participant,Integer> result = event.calculateTotalOwned();

        assertEquals(result.get(a),16);
        assertEquals(result.get(b),0);
        assertEquals(result.get(e),20);
        // for now that's all we can test since there is no settling the debt mechanic
      
    }

    @Test
    void individualOwnedTest()
    {
        Event event = new Event("z");
        Participant a = new Participant();
        Participant b = new Participant();
        Participant c = new Participant();
        Participant d = new Participant();
        Participant e = new Participant();
        event.addParticipant(a);
        event.addParticipant(b);
        event.addParticipant(c);
        event.addParticipant(d);
        event.addParticipant(e);
        Expense expense = new Expense("TEST",20,e);
        Expense expense2 = new Expense("TEST",16,a);
        event.addExpense(expense);
        event.addExpense(expense2);

        Map<Participant,Map<Participant,Integer>> result = event.calculateTotalOwned();
        
        assertEquals(result.get(a).get(e),4);
        assertEquals(result.get(a).get(b),4);
        assertEquals(result.get(b).get(a),0);
        assertEquals(result.get(b).get(e),0);
        assertEquals(result.get(e).get(a),5);
        assertEquals(result.get(e).get(b),5);
    }
}
