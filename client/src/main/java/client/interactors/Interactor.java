package client.interactors;

public interface Interactor {
    String getText(Object o);
    void clear(Object o);
    void setText(Object o, String s);
    <T> void select(Object o, T t);
    void createAlert(String s);
}
