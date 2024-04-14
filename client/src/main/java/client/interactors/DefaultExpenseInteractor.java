package client.interactors;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class DefaultExpenseInteractor implements ExpenseInteractor {

    @Override
    public String getText(Object o) {
        TextField textField = (TextField) o;
        return textField.getText();
    }

    @Override
    public void clear(Object o) {
        TextField textField = (TextField) o;
        textField.clear();
    }
    @Override
    public void setText(Object o, String s) {
        TextField textField = (TextField) o;
        textField.setText(s);
    }

    @Override
    public <T> void select(Object o, T t) {
        ComboBox<T> cb = (ComboBox<T>) o;
        cb.getSelectionModel().select(t);
    }
}
