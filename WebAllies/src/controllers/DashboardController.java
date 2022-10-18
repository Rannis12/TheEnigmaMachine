package controllers;

import dtos.web.ContestDetailsDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import logic.BattleFieldRefresher;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static util.Constants.CONTEST_DATA_PAGE_FXML_RESOURCE_LOCATION;
import static util.Constants.REFRESH_RATE;

public class DashboardController {

    private AlliesController alliesController;

    private ObservableList<String> battleFieldNames;
    private TimerTask battleFieldListRefresher;
    private Timer timer;
//    private final BooleanProperty autoUpdate;

    @FXML private FlowPane activeContestsFP;
    @FXML private ChoiceBox<String> ContestsCB;
    @FXML private Button readyBtn;
    @FXML private Label errorLabel;

    public DashboardController(){
//        autoUpdate = new SimpleBooleanProperty();
        battleFieldNames = FXCollections.observableArrayList();
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

    private void updateBattleFieldList(List<ContestDetailsDTO> dtoList) {
        Platform.runLater(() -> {
            updateFlowPane(dtoList);
        });
    }

    private void updateFlowPane(List<ContestDetailsDTO> dtoList) {

        activeContestsFP.getChildren().clear();
        for (ContestDetailsDTO dto : dtoList) {

            if(dto != null){
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource(CONTEST_DATA_PAGE_FXML_RESOURCE_LOCATION);
                fxmlLoader.setLocation(url);
                try {
                    Parent root = fxmlLoader.load(url.openStream());
                    UBoatConfigurationController controller = fxmlLoader.getController();

                    controller.setBattleFieldNameLabel(dto.getBattleFieldName());
                    controller.setUsernameLabel(dto.getUsername());
                    controller.setStatusLabel(dto.getGameStatus());
                    controller.setLevelLabel(dto.getDifficulty());
                    controller.setTeamsLabel(dto.getTeams());

                    activeContestsFP.getChildren().add(root);


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public void startBattleFieldListRefresher() {
        battleFieldListRefresher = new BattleFieldRefresher(
                /*autoUpdate,*/
                this::updateBattleFieldList);
        timer = new Timer();
        timer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }
}
