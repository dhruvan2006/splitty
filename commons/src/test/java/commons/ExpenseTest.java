package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

class ExpenseTest {
    @Test
    void testConstructor() {
        Expense e = new Expense();
        assertNotNull(e);
    }

    @Test
    void testGetID_testSetID() {
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        e.setId(13);
        assertEquals(13, e.getId());
    }

    @Test
    void testGetTitle() {
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        assertEquals("title", e.getTitle());
    }

    @Test
    void testSetTitle() {
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        e.setTitle("q");
        assertEquals("q", e.getTitle());
    }

    @Test
    void testGetTotal() {
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        assertEquals(1200, e.getTotalExpense());
    }

    @Test
    void testSetTotal() {
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        e.setTotalExpense(1300);
        assertEquals(1300, e.getTotalExpense());
    }

    @Test
    void testGetCreator() {
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        assertEquals(p, e.getCreator());
    }

    @Test
    void testSetCreator() {
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        Participant q = new Participant("qZ", "qX", "qZX");
        e.setCreator(q);
        assertEquals(q, e.getCreator());
    }

    @Test
    void testEqualsTrue() {
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        assertEquals(e, e);
    }

    @Test
    void testEqualsFalse() {
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        Expense e1 = new Expense("tittttle", 1800, p);
        assertNotEquals(e, e1);
        assertNotEquals(e1, e);
    }

    @Test
    void testHashCode() {
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        Expense e1 = new Expense("tittttle", 1800, p);
        assertEquals(e.hashCode(), e.hashCode());
        assertNotEquals(e1.hashCode(), e.hashCode());
    }

    @Test
    public void testToString() {
        Participant participant = new Participant("email@example.com", "IBAN123", "username");
        Event event = new Event("Event Title");
        Expense expense = new Expense("Expense Title", 100, participant, event);
        String expectedString = "Expense{id=0, creator=Participant{id=0, email='email@example.com', IBAN='IBAN123', userName='username'}, event=Event{id=0, title='Event Title'}, totalExpense=100, title='Expense Title'}";
        assertEquals(true, expense.toString().contains("Expense{id="));
        assertEquals(true, expense.toString().contains("creator=Participant{id="));
        assertEquals(true,expense.toString().contains(", event=Event{id="));
        assertEquals(true,expense.toString().contains(", totalExpense=100, title='Expense Title'}"));
    }


    @Test
    public void testEquals_SameAttributes() {

        Participant p = new Participant("Mara@g.c", "123", "MT");
        Expense expense1 = new Expense("Expense Title", 100, participant);
        Expense expense2 = new Expense("Expense Title", 100, participant);

        assertEquals(expense1, expense2);
    }

    @Test
    public void testEquals_DifferentAttributes() {

        Participant p = new Participant("Mara@g.c", "123", "MT");
        Participant h = new Participant("Hugh@g.c", "123", "HJ");
        Expense expense1 = new Expense("Expense Title", 100, p);
        Expense expense2 = new Expense("Different Title", 200, h);


        assertNotEquals(expense1, expense2);
    }

    @Test
    public void testGetSharePerPerson() {
        Participant p = new Participant("Mara@g.c", "123", "MT");
        Expense expense = new Expense("Expense Title", 100, participant);
        assertEquals(20, expense.getSharePerPerson(5));
    }





}
