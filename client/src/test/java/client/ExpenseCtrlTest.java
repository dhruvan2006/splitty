package client;

import client.scenes.ExpensesCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import client.utils.ServerUtilsInterface;
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ExpenseCtrlTest{
    ExpensesCtrl expensesCtrl;
    @Test
    public void setExpenseTest() {
        ServerUtils su = new ServerUtils();
        MainCtrl mainCtrl = new MainCtrl();
        expensesCtrl = new ExpensesCtrl(su, mainCtrl);
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        expensesCtrl.setExpense(e);
    }

    @Test
    public void getExpenseTest() {
        ServerUtils su = new ServerUtils();
        MainCtrl mainCtrl = new MainCtrl();
        expensesCtrl = new ExpensesCtrl(su, mainCtrl);
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        List<Participant> participants = new ArrayList<Participant>();
        participants.add(p);
        Expense expense = expensesCtrl.getExpenses(participants, "ZX", "1", "L");
        assert(Objects.equals(expense, new Expense("L", 100, p)));
    }
    @Test void modifyTest() {
        ServerUtilsInterface su = new MockServerUtils();
        MainCtrl mainCtrl = new MainCtrl();
        expensesCtrl = new ExpensesCtrl(su, mainCtrl);
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        List<Participant> participants = new ArrayList<Participant>();
    }
    @Test void updateLastUsedTest() {
        ServerUtilsInterface su = new MockServerUtils();
        MainCtrl mainCtrl = new MainCtrl();
        expensesCtrl = new ExpensesCtrl(su, mainCtrl);
        Participant p = new Participant("Z", "X", "ZX");
        Expense e = new Expense("title", 1200, p);
        List<Participant> participants = new ArrayList<Participant>();
        expensesCtrl.setEvent(new Event("some event"));
        expensesCtrl.updateLastUsed();
    }
}
