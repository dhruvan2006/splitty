package client;

import client.utils.ServerUtilsInterface;
import commons.Event;
import commons.Expense;

public class MockServerUtils implements ServerUtilsInterface {
    @Override
    public Expense addExpense(Expense modify) {
        return null;
    }

    @Override
    public Expense putExpense(long id, Expense modify) {
        return null;
    }

    @Override
    public Event updateLastUsedDate(Long id) {
        return null;
    }
}
