package commons;
import java.util.*;
public class Participent extends Person{
    Map<String,Integer> moneySpentFor;

    public Participent(String firstName,
                       String lastName,Map<String, Integer> moneySpentFor){
        super(firstName, lastName);
        this.moneySpentFor = moneySpentFor;

    }

    public Map<String, Integer> getMoneySpentFor() {
        return moneySpentFor;
    }

    public void setMoneySpentFor(Map<String, Integer> moneySpentFor) {
        this.moneySpentFor = moneySpentFor;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participent that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getMoneySpentFor(), that.getMoneySpentFor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMoneySpentFor());
    }

    @Override
    public String toString() {
        return "Participent{" +
                "moneySpentFor=" + moneySpentFor +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
