package commons;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
public class ParticipantTest {

    @Test
    public void testGetMoneySpentFor() {
        Map<String, Integer> moneySpentFor = new HashMap<>();
        moneySpentFor.put("Theme1", 100);
        Participent participent = new Participent("John", "Doe", moneySpentFor);
        assertEquals(moneySpentFor, participent.getMoneySpentFor());
    }

    @Test
    public void testSetMoneySpentFor() {
        Map<String, Integer> moneySpentFor = new HashMap<>();
        moneySpentFor.put("Theme1", 100);
        Participent participent = new Participent("John", "Doe", new HashMap<>());
        participent.setMoneySpentFor(moneySpentFor);
        assertEquals(moneySpentFor, participent.getMoneySpentFor());
    }

    @Test
    public void testMoneyForTheme_ExistingTheme() {
        Map<String, Integer> moneySpentFor = new HashMap<>();
        moneySpentFor.put("Theme1", 100);
        Participent participent = new Participent("John", "Doe", moneySpentFor);
        assertEquals(100, participent.moneyForTheme("Theme1"));
    }

    @Test
    public void testMoneyForTheme_NonExistingTheme() {
        Participent participent = new Participent("John", "Doe", new HashMap<>());
        assertEquals(-1, participent.moneyForTheme("NonExistingTheme"));
    }

    @Test
    public void testAddTheme_ExistingTheme() {
        Map<String, Integer> moneySpentFor = new HashMap<>();
        moneySpentFor.put("Theme1", 100);
        Participent participent = new Participent("John", "Doe", moneySpentFor);
        participent.addTheme("Theme1", 50);
        assertEquals(150, participent.moneyForTheme("Theme1"));
    }

    @Test
    public void testAddTheme_NonExistingTheme() {
        Participent participent = new Participent("John", "Doe", new HashMap<>());
        participent.addTheme("NewTheme", 50);
        assertEquals(50, participent.moneyForTheme("NewTheme"));
    }

    @Test
    public void testGetTotalSpent() {
        Map<String, Integer> moneySpentFor = new HashMap<>();
        moneySpentFor.put("Theme1", 100);
        moneySpentFor.put("Theme2", 200);
        Participent participent = new Participent("John", "Doe", moneySpentFor);
        assertEquals(300, participent.getTotalSpent());
    }


    @Test
    public void testEquals_EqualObjects() {
        Map<String, Integer> moneySpentFor1 = new HashMap<>();
        moneySpentFor1.put("Theme1", 100);
        Participent participent1 = new Participent("John", "Doe", moneySpentFor1);

        Map<String, Integer> moneySpentFor2 = new HashMap<>();
        moneySpentFor2.put("Theme1", 100);
        Participent participent2 = new Participent("John", "Doe", moneySpentFor2);

        assertTrue(participent1.equals(participent2));
    }

    @Test
    public void testEquals_DifferentObjects() {
        Participent participent1 = new Participent("John", "Doe", new HashMap<>());
        Participent participent2 = new Participent("Jane", "Doe", new HashMap<>());
        assertFalse(participent1.equals(participent2));
    }

    @Test
    public void testHashCode() {
        Map<String, Integer> moneySpentFor = new HashMap<>();
        moneySpentFor.put("Theme1", 100);
        Participent participent = new Participent("John", "Doe", moneySpentFor);
        assertEquals(moneySpentFor.hashCode(), participent.hashCode());
    }

    @Test
    public void testToString() {
        Map<String, Integer> moneySpentFor = new HashMap<>();
        moneySpentFor.put("Theme1", 100);
        Participent participent = new Participent("John", "Doe", moneySpentFor);
        String expectedToString = "Participent{" +
                "moneySpentFor=" + moneySpentFor +
                ", id=" + participent.getId() +
                ", firstName='" + participent.getFirstName() + '\'' +
                ", lastName='" + participent.getLastName() + '\'' +
                '}';
        assertEquals(expectedToString, participent.toString());
    }
}
