package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @OneToMany(mappedBy = "expense")
    ArrayList<ExpensePayed> expensesPayed;

    @ManyToOne()
    Participant creator;

    @ManyToOne()
    Event event; // the event this expense belongs to

    public Expense() {
    }

    //to indicate if object ready to be added to database
    public boolean isInit(){
        return true;
    }

    private int totalExpense;

    private String title;

    public Expense(String title, int totalExpense, Participant creator) {
        this.title = title;
        this.totalExpense = totalExpense;
        this.creator = creator;
        this.expensesPayed = new ArrayList<>();
    }

    public Event getEvent() {
        return event;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setCreator(Participant creator) {
        this.creator = creator;
    }

    private ArrayList<ExpensePayed> getExpensesPayed() {
        return expensesPayed;
    }

    public int getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(int totalExpense) {
        this.totalExpense = totalExpense;
    }

    public List<Participant> getParticipantsWhoPayed(){
        return expensesPayed.stream().filter((expensePayed -> expensePayed.payed))
                .map((expensePayed -> expensePayed.participant))
                .collect(Collectors.toList());
    }

    public List<Participant> getParticipantsWhoNotPayed(){
        return expensesPayed.stream().filter((expensePayed -> !expensePayed.payed))
                .map((expensePayed -> expensePayed.participant))
                .collect(Collectors.toList());
    }

    public int hashCode() {
        return Objects.hash(id, title, creator);//, expenses);
    }

    @Override
    public String toString() {
        return "Expense " + id +
                " - " + title + '\'' +
                "created by " + creator.getFullName() + '\'';
    }
}

