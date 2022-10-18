package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;

public class AlliesController {

    private AlliesMainAppController alliesMainAppController;
    @FXML private DashboardController dashboardController;
    @FXML private ContestController contestController;

    @FXML private Label userLabel;

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
    }
}

