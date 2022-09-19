package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ConfigurationController {

    @FXML private Label toEncodeLabel;
    @FXML private Label decodedToLabel;
    @FXML private Label agentIDLabel;
    @FXML private Label configurationLabel;
    @FXML private Label rotorsPositionsLabel;
    @FXML private Label reflectorIDLabel;
    @FXML private Label timeTakenLabel;

    public void setToEncodeLabel(String toEncodeLabel) {
        this.toEncodeLabel.setText(toEncodeLabel);
    }

    public void setDecodedToLabel(String decodedToLabel) {
        this.decodedToLabel.setText(decodedToLabel);
    }

    public void setAgentIDLabel(String agentIDLabel) {
        this.agentIDLabel.setText(agentIDLabel);
    }

    public void setConfigurationLabel(String configurationLabel) {
        this.configurationLabel.setText(configurationLabel);
    }

    public void setRotorsPositionsLabel(String rotorsPositionsLabel) {
        this.rotorsPositionsLabel.setText(rotorsPositionsLabel);
    }

    public void setReflectorIDLabel(String reflectorIDLabel) {
        this.reflectorIDLabel.setText(reflectorIDLabel);
    }

    public void setTimeTakenLabel(String timeTakenLabel){
        this.timeTakenLabel.setText(timeTakenLabel);
    }
}
