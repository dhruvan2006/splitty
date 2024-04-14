package client.interactors;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class DefaultInteractor implements Interactor {

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

    @Override
    public void createAlert(String s) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
