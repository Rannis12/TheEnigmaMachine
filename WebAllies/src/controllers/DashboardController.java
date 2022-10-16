package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class DashboardController {

    private AlliesController alliesController;
    @FXML private FlowPane activeContestsFP;
    @FXML private ChoiceBox<?> ContestsCB;
    @FXML private Button readyBtn;
    @FXML private Label errorLabel;


    @FXML
    void readyBtnListener(ActionEvent event) {

    }

    public void setAlliesController(AlliesController alliesController) {
        this.alliesController = alliesController;
    }
}
