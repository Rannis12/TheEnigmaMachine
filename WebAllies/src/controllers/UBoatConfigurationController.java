package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UBoatConfigurationController {

    @FXML private Label battleFieldNameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label statusLabel;
    @FXML private Label levelLabel;
    @FXML private Label teamsLabel;


    public void setBattleFieldNameLabel(String battleFieldNameLabel) {
        this.battleFieldNameLabel.setText(battleFieldNameLabel);
    }

    public void setUsernameLabel(String usernameLabel) {
        this.usernameLabel.setText(usernameLabel);
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel.setText(statusLabel);
    }

    public void setLevelLabel(String levelLabel) {
        this.levelLabel.setText(levelLabel);
    }

    public void setTeamsLabel(String teamsLabel) {
        this.teamsLabel.setText(teamsLabel);
    }
}
