package client.scenes;

import client.utils.ServerUtils;
import commons.PasswordGenerator;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import javax.inject.Inject;
import java.util.List;

public class JoinAdminCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField password;

    private int saltHash;

    @Inject
    public JoinAdminCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void join(){
        String x = password.getText() + "55005";
        if(x.hashCode() == saltHash){
            password.clear();
            mainCtrl.showAdminScreen();
        }
        else{
            mainCtrl.showNotification("Wrong Password", "#FF6666");
            password.setStyle("-fx-border-color: #FF6666");
        }
    }

    public void setSaltHash() {

    }

    public int getSaltHash() {
        return saltHash;
    }
}