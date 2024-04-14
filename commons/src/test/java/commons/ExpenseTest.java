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

//    @Test
//    void testToString() {
//        Participant p = new Participant("Mara", "Tacenco", "MT");
//        Expense e = new Expense("Brunch in Delft", 1200, p);
//        assertTrue(e.toString().contains("Expense "));
//        assertTrue(e.toString().contains("- Brunch in Delft, created by "));
//    }

    @Test
    void testGetParticipants() {

        Event event = new Event("X");
        Participant p = new Participant("Mara", "Tacenco", "MT");
        Participant h = new Participant("Hugh", "Jones", "HJ");
        List<Participant> list = new ArrayList<>();
        list.add(p);
        list.add(h);

        Expense e = new Expense("Brunch in Delft", 1200, p ,event);
    }

    @Test
    void testSetParticipants() {

        Event event = new Event("X");
        Participant p = new Participant("Mara", "Tacenco", "MT");
        Participant h = new Participant("Hugh", "Jones", "HJ");

        List<Participant> list = new ArrayList<>();
        list.add(p);
        list.add(h);

        Expense e = new Expense("Brunch in Delft", 1200, p ,event);

        List<Participant> list2 = new ArrayList<>();
        list2.add(h);
    }

    @Test
    public void testEquals_SameAttributes() {

        Participant participant = new Participant("email@example.com", "IBAN123", "username");
        Expense expense1 = new Expense("Expense Title", 100, participant);
        Expense expense2 = new Expense("Expense Title", 100, participant);

        assertEquals(expense1, expense2);
    }

    @Test
    public void testEquals_DifferentAttributes() {

        Participant participant1 = new Participant("email1@example.com", "IBAN123", "username1");
        Participant participant2 = new Participant("email2@example.com", "IBAN456", "username2");
        Expense expense1 = new Expense("Expense Title", 100, participant1);
        Expense expense2 = new Expense("Different Title", 200, participant2);


        assertNotEquals(expense1, expense2);
    }




}
