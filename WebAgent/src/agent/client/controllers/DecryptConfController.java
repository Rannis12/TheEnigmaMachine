package agent.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DecryptConfController {

    @FXML private Label decodedToLabel;
    @FXML private Label agentIDLabel;

    @FXML private Label rotorsPositionsAndOrderLabel;
    @FXML private Label reflectorIDLabel;

    public void setRotorsPositionsAndOrderLabel(String rotorsPositionsAndOrderLabel) {
        this.rotorsPositionsAndOrderLabel.setText(rotorsPositionsAndOrderLabel);
    }
    public void setDecodedToLabel(String decodedToLabel) {
        this.decodedToLabel.setText(decodedToLabel);
    }

    public void setAgentLabel(String agentIDLabel) {
        this.agentIDLabel.setText(agentIDLabel);
    }

    public void setReflectorIDLabel(String reflectorIDLabel) {
        this.reflectorIDLabel.setText(reflectorIDLabel);
    }

}
