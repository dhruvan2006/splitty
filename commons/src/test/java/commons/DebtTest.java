package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DebtTest {
    @Test
    void constructorNotNull(){
        Debt debt = new Debt(0, 0, null, null, "EUR", null);
        assertNotNull(debt);
    }
    @Test
    void getterAmount(){
        Debt debt = new Debt(0, 0, null, null, "EUR", null);
        assertEquals(debt.getAmount(), 0);
    }
    @Test
    void getterPersonsFrom(){
        Participent[] participents = new Participent[3];
        participents[0] = new Participent("Eduardas", "Eduardas", null);
        participents[1] = new Participent("Eduardas", "Eduardas", null);
        participents[2] = new Participent("Eduardas", "Eduardas", null);
        Debt debt = new Debt(0, 0, participents, null, "EUR", null);
        assertEquals(debt.getPersonsOwnsFrom(), participents);
    }
    @Test
    void getterPersonsTo(){
        Participent[] participents = new Participent[3];
        participents[0] = new Participent("Eduardas", "Eduardas", null);
        participents[1] = new Participent("Eduardas", "Eduardas", null);
        participents[2] = new Participent("Eduardas", "Eduardas", null);
        Debt debt = new Debt(0, 0, participents, participents[0], "EUR", null);
        assertEquals(debt.getPersonOwnsTo(), participents[0]);
    }
    @Test
    void toStringTest() {
        Participent[] participents = new Participent[3];
        participents[0] = new Participent("Eduardas", "Eduardas", null);
        participents[1] = new Participent("Eduardas", "Eduardas", null);
        participents[2] = new Participent("Eduardas", "Eduardas", null);
        Debt debt = new Debt(0, 0, participents, participents[0], "EUR", null);
        String output = "Debt: from persons, ";
        for (Participent person : debt.getPersonsOwnsFrom()) {
            output += person;
            output += " ";
        }
        output += "to person: ";
        output += debt.getPersonOwnsTo();
        output += " 0 of EUR debt not settled";
        assertEquals(output, debt.toString());
    }
    @Test
    void toStringAfterSettleTest() {
        Participent[] participents = new Participent[3];
        participents[0] = new Participent("Eduardas", "Eduardas", null);
        participents[1] = new Participent("Eduardas", "Eduardas", null);
        participents[2] = new Participent("Eduardas", "Eduardas", null);
        Debt debt = new Debt(0, 0, participents, participents[0], "EUR", null);
        String output = "Debt: from persons, ";
        for (Participent person : debt.getPersonsOwnsFrom()) {
            output += person;
            output += " ";
        }
        output += "to person: ";
        output += debt.getPersonOwnsTo();
        output += " 0 of EUR settled";
        debt.settleIt();
        assertEquals(output, debt.toString());
    }
}
