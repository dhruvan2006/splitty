package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testToString() {
        Participant p = new Participant("Mara", "Tacenco", "MT");
        Expense e = new Expense("Brunch in Delft", 1200, p);
        assertTrue(e.toString().contains("Expense "));
        assertTrue(e.toString().contains("- Brunch in Delft, created by "));
    }

    @Test
    void testGetParticipants() {

        Event event = new Event("X");
        Participant p = new Participant("Mara", "Tacenco", "MT");
        Participant h = new Participant("Hugh", "Jones", "HJ");
        List<Participant> list = new ArrayList<>();
        list.add(p);
        list.add(h);

        Expense e = new Expense("Brunch in Delft", 1200, p ,event,list);

        assertEquals(list,e.getParticipants());
    }

    @Test
    void testSetParticipants() {

        Event event = new Event("X");
        Participant p = new Participant("Mara", "Tacenco", "MT");
        Participant h = new Participant("Hugh", "Jones", "HJ");

        List<Participant> list = new ArrayList<>();
        list.add(p);
        list.add(h);

        Expense e = new Expense("Brunch in Delft", 1200, p ,event,list);

        List<Participant> list2 = new ArrayList<>();
        list2.add(h);

        e.setParticipants(list2);

        assertEquals(list2,e.getParticipants());
    }





}
