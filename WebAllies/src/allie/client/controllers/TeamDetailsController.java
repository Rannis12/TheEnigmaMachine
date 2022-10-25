package allie.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TeamDetailsController {

    @FXML private Label teamNameLabel;
    @FXML private Label agentAmountLabel;
    @FXML private Label missionSizeLabel;


    public void setTeamNameLabel(String teamNameLabel) {
        this.teamNameLabel.setText(teamNameLabel);
    }

    public void setAgentAmountLabel(String agentAmountLabel) {
        this.agentAmountLabel.setText(agentAmountLabel);
    }

    public void setMissionSizeLabel(String missionSizeLabel) {
        this.missionSizeLabel.setText(missionSizeLabel);
    }
}
