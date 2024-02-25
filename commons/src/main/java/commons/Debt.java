package commons;

import java.util.Arrays;
import java.util.Objects;

public class Debt {
    private int id_debt;
    private int amount;
    //should be class person but on this stage of work it does not finished
    private String[] personsOwnsFrom;
    private String personOwnsTo;

    private String currency;


    public Debt(int id_debt, int amount, String[] personsOwnsFrom, String personOwnsTo, String currency) {
        this.id_debt = id_debt;
        this.amount = amount;
        this.personsOwnsFrom = personsOwnsFrom;
        this.personOwnsTo = personOwnsTo;
        this.currency = currency;
    }

    public int getId_debt() {
        return id_debt;
    }

    public int getAmount() {
        return amount;
    }

    public String[] getPersonsOwnsFrom() {
        return personsOwnsFrom;
    }

    public String getPersonOwnsTo() {
        return personOwnsTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if(o == null)
            return false;

        if (o.getClass() != this.getClass())
            return false;

        Debt debt = (Debt) o;
        boolean eq = true;
        eq &= (id_debt == debt.id_debt && amount == debt.amount);
        eq &= (Arrays.equals(personsOwnsFrom, debt.personsOwnsFrom) && Objects.equals(personOwnsTo, debt.personOwnsTo));
        return eq;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id_debt, amount, personOwnsTo);
        result = 31 * result + Arrays.hashCode(personsOwnsFrom);
        return result;
    }

    @Override
    public String toString() {
        String output = "";
        output += "Debt: from persons, ";
        for (String person : personsOwnsFrom) {
            output += person;
            output += " ";
        }
        output += "to person: ";
        output += personOwnsTo;
        output += " ";
        output += amount;
        output += " of ";
        output += currency;
        return output;
    }
}
