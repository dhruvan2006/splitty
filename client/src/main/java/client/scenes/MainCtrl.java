/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;


import commons.Event;
import javafx.animation.PauseTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;


public class MainCtrl {

    private JoinAdminCtrl joinAdminCtrl;
    private Scene joinAdmin;

    private Stage primaryStage;

    private OverviewCtrl overviewCtrl;
    private Scene overview;


    private ParticipantCtrl participantCtrl;
    private Scene configParticipant;

    private StartScreenCtrl startScreenCtrl;
    private Scene start;

    private AdminCtrl adminCtrl;
    private Scene admin;

    public void initialize(Stage primaryStage, Pair<StartScreenCtrl, Parent> start, Pair<ParticipantCtrl, Parent> cParticipant, Pair<OverviewCtrl, Parent> overview, Pair<AdminCtrl, Parent> admin, Pair<JoinAdminCtrl, Parent> joinAdmin) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
        this.startScreenCtrl = start.getKey();
        this.start = new Scene(start.getValue());
        this.participantCtrl = cParticipant.getKey();
        this.configParticipant = new Scene(cParticipant.getValue());
        this.adminCtrl = admin.getKey();
        this.admin = new Scene(admin.getValue());
        this.joinAdminCtrl = joinAdmin.getKey();
        this.joinAdmin = new Scene(joinAdmin.getValue());
        showStartScreen();
        primaryStage.show();
    }

    public void initialize2(Stage primaryStage, Pair<OverviewCtrl, Parent> startScreen){
        this.primaryStage = primaryStage;
        this.overviewCtrl = startScreen.getKey();
        this.start = new Scene(startScreen.getValue());
        showStartScreen();
        primaryStage.show();
    }

    public ParticipantCtrl getParticipantCtrl() {
        return participantCtrl;
    }

    public void showStartScreen(){
        primaryStage.setTitle("StartScreen");
        startScreenCtrl.updateRecentEvents();
        primaryStage.setScene(start);
    }


    public void showAdminScreen() {
        primaryStage.setTitle("Admin");
        primaryStage.setScene(admin);

    }

    public void showJoinAdmin(){
        primaryStage.setTitle("Join Admin");
        primaryStage.setScene(joinAdmin);
    }

    public void showConfigParticipant(OverviewCtrl overviewCtrl) {
        participantCtrl.setOverviewCtrl(overviewCtrl);
        primaryStage.setTitle("Participant config");
        primaryStage.setScene(configParticipant);
    }

    public void showOverview() {
        primaryStage.setTitle("Overview");
        primaryStage.setScene(overview);
    }

    public void showOverviewWithEvent(Event event) {
        overviewCtrl.setEvent(event);
        overviewCtrl.initialize();
        showOverview();
    }

    public void showScene(Scene scene, String title) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
    }



    public OverviewCtrl getOverviewCtrl() {
        return overviewCtrl;
    }

    public void showNotification(String message, String color) {
        Popup popup = new Popup();
        Label label = new Label(message);
        label.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10;");
        popup.getContent().add(label);
        // position top-right
        popup.setOnShown(event -> {
            popup.setX(primaryStage.getX() + primaryStage.getWidth() - popup.getWidth() - 20);
            popup.setY(primaryStage.getY() + 40);
        });
        popup.show(primaryStage);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }

}
