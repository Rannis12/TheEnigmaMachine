package allie.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class AlliesController {

    private AlliesMainAppController alliesMainAppController;
    @FXML private DashboardController dashboardController;
    @FXML private ContestController contestController;

    @FXML private Label userLabel;
    @FXML private Tab dashboardTab;

    @FXML
    public void initialize(){
        if(dashboardController != null && contestController != null) {
            dashboardController.setAlliesController(this);
            contestController.setAlliesController(this);
        }
    }


    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.alliesMainAppController = alliesMainAppController;
    }

    public Label getUserLabel() {
        return userLabel;
    }

    public void setActive() {
        dashboardController.startBattleFieldListRefresher();
        dashboardController.startTableViewRefresher();
    }

    public String getCurrentUserName() {
        return alliesMainAppController.getCurrentUserName();
    }

    public void setDashboardTabDisable(boolean b){
        dashboardTab.setDisable(b);
    }

    public void startBattleFieldListForContestController(String battleName) {
        contestController.startBattleFieldListRefresher(battleName); //refreshed details component
        contestController.startTeamInformationRefresher(battleName); //refreshed team information component
        contestController.startAgentsTableViewRefresher(); //refreshed active agent component
        contestController.startShouldStartContestRefresher(battleName);
    }

    public void popUpError(String errorMsg) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid input");
        errorAlert.setContentText(errorMsg);
        errorAlert.showAndWait();
    }
}

