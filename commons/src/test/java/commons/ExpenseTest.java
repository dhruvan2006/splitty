package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpenseTest {
    @Test
    void testConstructor() {
        Expense e = new Expense();
        assertNotNull(e);
    }

    @Test
    void testGetID_testSetID() {
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
        Expense e = new Expense("title", 1200, p);
        e.setId(13);
        assertEquals(13, e.getId());
    }

    @Test
    void testGetTitle() {
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
        Expense e = new Expense("title", 1200, p);
        assertEquals("title", e.getTitle());
    }

    @Test
    void testSetTitle() {
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
        Expense e = new Expense("title", 1200, p);
        e.setTitle("q");
        assertEquals("q", e.getTitle());
    }

    @Test
    void testGetTotal() {
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
        Expense e = new Expense("title", 1200, p);
        assertEquals(1200, e.getTotalExpense());
    }

    @Test
    void testSetTotal() {
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
        Expense e = new Expense("title", 1200, p);
        e.setTotalExpense(1300);
        assertEquals(1300, e.getTotalExpense());
    }

    @Test
    void testGetCreator() {
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
        Expense e = new Expense("title", 1200, p);
        assertEquals(p, e.getCreator());
    }

    void testSetCreator() {
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
        Expense e = new Expense("title", 1200, p);
        Participant q = new Participant("qZ", "qX", "qL", "qE", "qZX");
        e.setCreator(q);
        assertEquals(q, e.getCreator());
    }

    @Test
    void testEqualsTrue() {
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
        Expense e = new Expense("title", 1200, p);
        assertEquals(e, e);
    }

    @Test
    void testEqualsFalse() {
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
        Expense e = new Expense("title", 1200, p);
        Expense e1 = new Expense("tittttle", 1800, p);
        assertNotEquals(e, e1);
        assertNotEquals(e1, e);
    }

    @Test
    void testHashCode() {
        Participant p = new Participant("Z", "X", "L", "E", "ZX");
        Expense e = new Expense("title", 1200, p);
        Expense e1 = new Expense("tittttle", 1800, p);
        assertEquals(e.hashCode(), e.hashCode());
        assertNotEquals(e1.hashCode(), e.hashCode());
    }
}
