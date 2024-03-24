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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private ParticipantCtrl participantCtrl;

    private Scene configParticipant;
    private StartScreenCtrl startScreenCtrl;
    private Scene start;

    public void initialize(Stage primaryStage, Pair<StartScreenCtrl, Parent> start) {
//            Pair<AddQuoteCtrl, Parent> add) {
        this.primaryStage = primaryStage;
        this.startScreenCtrl = start.getKey();
        this.start = new Scene(start.getValue());
        showStartScreen();
        primaryStage.show();
    }

    public void initialize2(Stage primaryStage, Pair<OverviewCtrl, Parent> startScreen){
        this.primaryStage = primaryStage;
        this.start = new Scene(startScreen.getValue());
        showStartScreen();
        primaryStage.show();
    }

    public ParticipantCtrl getParticipantCtrl() {
        return participantCtrl;
    }

    public void showStartScreen(){
        primaryStage.setTitle("StartScreen");
        primaryStage.setScene(start);
        startScreenCtrl.updateEventsList();
    }


    public void showConfigParticipant(OverviewCtrl overviewCtrl) {
        participantCtrl.setOverviewCtrl(overviewCtrl);
        primaryStage.setTitle("Participant config");
        primaryStage.setScene(configParticipant);
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

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }


    public Event getCurrent() {
        return current;
    }
}
