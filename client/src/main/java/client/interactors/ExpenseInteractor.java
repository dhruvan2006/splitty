package client.interactors;

public interface ExpenseInteractor {
    String getText(Object o);
    void clear(Object o);
    void setText(Object o, String s);
    <T> void select(Object o, T t);
}
