package client.utils;

import commons.Event;
import commons.Expense;

public interface ServerUtilsInterface {
    Expense addExpense(Expense modify);

    Expense putExpense(long id, Expense modify);

    Event updateLastUsedDate(Long id);
}
