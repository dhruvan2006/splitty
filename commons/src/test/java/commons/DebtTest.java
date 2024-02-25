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
        Participant[] participants = new Participant[3];
        participants[0] = new Participant("Eduardas", "Eduardas");
        participants[1] = new Participant("Eduardas", "Eduardas");
        participants[2] = new Participant("Eduardas", "Eduardas");
        Debt debt = new Debt(0, 0, participants, null, "EUR", null);
        assertEquals(debt.getPersonsOwnsFrom(), participants);
    }
    @Test
    void getterPersonsTo(){
        Participant[] participants = new Participant[3];
        participants[0] = new Participant("Eduardas", "Eduardas");
        participants[1] = new Participant("Eduardas", "Eduardas");
        participants[2] = new Participant("Eduardas", "Eduardas");
        Debt debt = new Debt(0, 0, participants, participants[0], "EUR", null);
        assertEquals(debt.getPersonOwnsTo(), participants[0]);
    }
    @Test
    void toStringTest() {
        Participant[] participants = new Participant[3];
        participants[0] = new Participant("Eduardas", "Eduardas");
        participants[1] = new Participant("Eduardas", "Eduardas");
        participants[2] = new Participant("Eduardas", "Eduardas");
        Debt debt = new Debt(0, 0, participants, participants[0], "EUR", null);
        String output = "Debt: from persons, ";
        for (Participant person : debt.getPersonsOwnsFrom()) {
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
        Participant[] participants = new Participant[3];
        participants[0] = new Participant("Eduardas", "Eduardas");
        participants[1] = new Participant("Eduardas", "Eduardas");
        participants[2] = new Participant("Eduardas", "Eduardas");
        Debt debt = new Debt(0, 0, participants, participants[0], "EUR", null);
        String output = "Debt: from persons, ";
        for (Participant person : debt.getPersonsOwnsFrom()) {
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
