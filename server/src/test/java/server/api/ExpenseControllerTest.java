package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import commons.Expense;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ExpenseControllerTest {

    @Test
    public void cannotAddNullExpense() {
        TestExpenseRepository repo = new TestExpenseRepository();
        ExpenseController sut = new ExpenseController(repo);
        var actual = sut.add(null, 1);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addExpenseToEvent() {
        TestExpenseRepository repo = new TestExpenseRepository();
        ExpenseController sut = new ExpenseController(repo);
        Expense expense = new Expense("Title", 100, "Creator");
        var actual = sut.add(expense, 1);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void deleteExpense() {
        TestExpenseRepository repo = new TestExpenseRepository();
        ExpenseController sut = new ExpenseController(repo);
        Expense expense = new Expense("Title", 100, "Creator");
        Expense posted = sut.add(expense, 1).getBody();
        assert posted != null;
        var actual = sut.deleteIt(posted.getId());
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void getById_ValidId_ReturnsExpense() {
        TestExpenseRepository repo = new TestExpenseRepository();
        ExpenseController sut = new ExpenseController(repo);
        Expense expense = new Expense("Title", 100, "Creator");
        Expense savedExpense = sut.add(expense, 1).getBody();
        var actual = sut.getById(String.valueOf(savedExpense.getId()));
        assertEquals(OK, actual.getStatusCode());
        List<Expense> expenses = actual.getBody();
        assert expenses != null;
        assertEquals(savedExpense, expenses.get(0));
    }

    @Test
    public void getById_InexistentId_ReturnsBadRequest() {
        TestExpenseRepository repo = new TestExpenseRepository();
        ExpenseController sut = new ExpenseController(repo);
        var actual = sut.getById("9999");
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getById_InvalidId_ReturnsBadRequest() {
        TestExpenseRepository repo = new TestExpenseRepository();
        ExpenseController sut = new ExpenseController(repo);
        var actual = sut.getById("abc");
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void updateExpense() {
        TestExpenseRepository repo = new TestExpenseRepository();
        ExpenseController sut = new ExpenseController(repo);
        Expense expense = new Expense("Title", 100, );
        Expense savedExpense = sut.add(expense, 1).getBody();
        Participant p = new Participant("john@example.com", "IBAN12345", "JD");
        assert savedExpense != null;

        savedExpense.setTitle("Updated Title");
        savedExpense.setTotalExpense(200);
        savedExpense.setCreator(p);

        var actual = sut.updateExpense(savedExpense.getId(), savedExpense);
        assertEquals(OK, actual.getStatusCode());

        Expense updatedExpense = actual.getBody();
        assert updatedExpense != null;
        assertEquals("Updated Title", updatedExpense.getTitle());
        assertEquals(200, updatedExpense.getTotalExpense());
        assertEquals(p, updatedExpense.getCreator());
    }
}
