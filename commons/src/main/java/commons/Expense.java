package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @OneToMany(mappedBy = "expense")
    Set<ExpensePayed> expensesPayed;

    @ManyToOne()
    Participant creator;

    @ManyToOne()
    Event event; // the event this expense belongs to

    private int totalExpense;

    private String title;

    public Event getEvent() {
        return event;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Participant getCreator() {
        return creator;
    }

    private Set<ExpensePayed> getExpensesPayed() {
        return expensesPayed;
    }

    public int getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(int totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Set<Participant> getParticipantsWhoPayed(){
        return expensesPayed.stream().filter((expensePayed -> expensePayed.payed))
                .map((expensePayed -> expensePayed.participant))
                .collect(Collectors.toSet());
    }

    public Set<Participant> getParticipantsWhoNotPayed(){
        return expensesPayed.stream().filter((expensePayed -> !expensePayed.payed))
                .map((expensePayed -> expensePayed.participant))
                .collect(Collectors.toSet());
    }

    public int hashCode() {
        return Objects.hash(id, title, creator);//, expenses);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", title='" + title + '\'' +
                "created by" + creator + '\'' +
                '}';
    }
}

