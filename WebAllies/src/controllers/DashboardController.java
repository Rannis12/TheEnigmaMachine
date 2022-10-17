package controllers;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import logic.BattleFieldRefresher;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static util.Constants.REFRESH_RATE;

public class DashboardController {

    private AlliesController alliesController;

    private ObservableList<String> battleFieldNames;
    private TimerTask battleFieldListRefresher;
    private Timer timer;
    private final BooleanProperty autoUpdate;

    @FXML private FlowPane activeContestsFP;
    @FXML private ChoiceBox<String> ContestsCB;
    @FXML private Button readyBtn;
    @FXML private Label errorLabel;

    public DashboardController(){
        autoUpdate = new SimpleBooleanProperty();
        battleFieldNames = new SimpleListProperty<>();
    }

    @FXML
    public void initialize(){
        ContestsCB.setItems(battleFieldNames); //maybe cause problems.
    }
    @FXML
    void readyBtnListener(ActionEvent event) {

    }

    public void setAlliesController(AlliesController alliesController) {
        this.alliesController = alliesController;
    }

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
                /*httpStatusUpdate::updateHttpLine,*/
                this::updateBattleFieldList);
        timer = new Timer();
        timer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }
}
