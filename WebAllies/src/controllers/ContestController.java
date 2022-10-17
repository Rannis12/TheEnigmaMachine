package controllers;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import logic.BattleFieldRefresher;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static util.Constants.REFRESH_RATE;

public class ContestController {

    private AlliesController alliesController;
//    private ListProperty battleFieldNames = new SimpleListProperty();
//    private ObservableList<String> battleFieldNames;
//    private TimerTask battleFieldListRefresher;
//    private Timer timer;
//        private final IntegerProperty totalUsers;
    @FXML private ChoiceBox<?> contestsCB;
    private final BooleanProperty autoUpdate;
    @FXML private Button processBtn;


    public ContestController(){
//        totalUsers = new SimpleIntegerProperty();
        autoUpdate = new SimpleBooleanProperty();
//        battleFieldNames = new SimpleListProperty<>();
    }

    @FXML
    public void initialize(){
//        contestsCB.setItems(battleFieldNames); //maybe cause problems.
    }

    @FXML
    void processBtnListener(ActionEvent event) {

    }


    public void setAlliesController(AlliesController alliesController){
        this.alliesController = alliesController;
    }

    //Ofek can ignore it
   /*
   private void updateBattleFieldList(List<String> bfNames) {
        Platform.runLater(() -> {
            battleFieldNames.clear();
//            items.clear();
            battleFieldNames.addAll(bfNames);
//            totalUsers.set(usersNames.size());
        });
    }

    public void startBattleFieldListRefresher() {
        battleFieldListRefresher = new BattleFieldRefresher(
                autoUpdate,
                *//*httpStatusUpdate::updateHttpLine,*//*
                this::updateBattleFieldList);
        timer = new Timer();
        timer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }*/

}
