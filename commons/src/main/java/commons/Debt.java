package commons;

import java.util.*;

public class Debt {
    private int id_debt;
    private int amount;
    private Participant[] personsOwnsFrom;
    private Participant personOwnsTo;
    private boolean settled;
    private String currency;
    private Optional<String> iban;
    private  Map<String, Integer> exchangeRate;


    public Debt(int id_debt, int amount, Participant[] personsOwnsFrom, Participant personOwnsTo, String currency,
        Map<String, Integer> exchangeRate) {
        this.id_debt = id_debt;
        this.amount = amount;
        this.personsOwnsFrom = personsOwnsFrom;
        this.personOwnsTo = personOwnsTo;
        this.currency = currency;
        settled = false;
        iban = Optional.empty();
        this.exchangeRate = exchangeRate;
    }

    public boolean isSettled() {
        return settled;
    }

    public String getCurrency() {
        return currency;
    }

    public int getId_debt() {
        return id_debt;
    }

    public int getAmount() {
        return amount;
    }

    public Participant[] getPersonsOwnsFrom() {
        return personsOwnsFrom;
    }

    public Participant getPersonOwnsTo() {
        return personOwnsTo;
    }

    public void settleIt() {settled = true;}
    public int rateConvert(String from, String to) {
        return exchangeRate.get(from)/exchangeRate.get(to);
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
        eq &= (Objects.equals(currency, debt.currency) && (settled == debt.settled));
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
        String output = "Debt: from persons, ";
        for (Participant person : personsOwnsFrom) {
            output += person;
            output += " ";
        }
        output += "to person: ";
        output += personOwnsTo;
        output += " ";
        output += amount;
        output += " of ";
        output += currency;
        if(!settled)
            output += " debt not settled";

        else
            output += " settled";
        return output;
    }

}
