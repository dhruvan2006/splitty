package client;

import client.interactors.Interactor;
import client.scenes.ExpensesCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtilsInterface;
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;


public class ExpenseCtrlTest{
    ExpensesCtrl expensesCtrl;
    Interactor interactor;
    ServerUtilsInterface su;
    MainCtrl mainCtrl;
    Participant p;
    Expense e;
    List<Participant> participants;
//    @BeforeEach
//    public void setup() {
//        interactor = new MockInteractor();
//        su = new MockServerUtils();
//        mainCtrl = new MainCtrl();
//        expensesCtrl = new ExpensesCtrl(su, mainCtrl, interactor);
//        p = new Participant("Z", "X", "ZX");
//        e = new Expense("title", 1200, p);
//        participants = new ArrayList<>();
//        participants.add(p);
//    }
    @Test
    public void setExpenseTest() {
        expensesCtrl.setExpense(e);
    }

    @Test
    public void getExpenseTest() {
        Expense expense = expensesCtrl.getExpenses(participants, "ZX", "1", "L");
        assert(Objects.equals(expense, new Expense("L", 100, p)));
    }
//    @Test void modifyTest() {
//        ServerUtilsInterface su = new MockServerUtils();
//        MainCtrl mainCtrl = new MainCtrl();
//        expensesCtrl = new ExpensesCtrl(su, mainCtrl, interactor);
//        Participant p = new Participant("Z", "X", "ZX");
//        Expense e = new Expense("title", 1200, p);
//        List<Participant> participants = new ArrayList<Participant>();
//    }
    @Test
    void updateLastUsedTest() {
        expensesCtrl.setEvent(new Event("some event"));
        expensesCtrl.updateLastUsed();
    }
    @Test
    void clearTest(){
        expensesCtrl.setExpense(e);
        expensesCtrl.clearFields();
    }
    @Test
    void initializeWithExpenseTest() {
        Expense e1 = new Expense("title", 1200, p);
        expensesCtrl.setExpense(e1);
        expensesCtrl.initializeWithExpense(e1);
    }
}
