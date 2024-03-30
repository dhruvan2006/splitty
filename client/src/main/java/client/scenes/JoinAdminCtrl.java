package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminPassword() {
        return adminPassword; //For testing
    }
}