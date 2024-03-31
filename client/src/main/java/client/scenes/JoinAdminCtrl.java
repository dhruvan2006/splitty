package client.scenes;

import client.utils.ServerUtils;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import javax.inject.Inject;

public class JoinAdminCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField password;

    private String adminPassword;

    @Inject
    public JoinAdminCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        setAdminPassword();
    }

    public void join(){
        String x = password.getText();
        if(x.equals(adminPassword)){
            password.clear();
            mainCtrl.showAdminScreen();
        }
        else{
            mainCtrl.showNotification("Wrong Password", "#FF6666");
            password.setStyle("-fx-border-color: #FF6666");
        }
    }

    public void setAdminPassword() {
        try{
            adminPassword = server.getPassword();
        }
        catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    public String getAdminPassword() {
        return adminPassword; //For testing
    }
}