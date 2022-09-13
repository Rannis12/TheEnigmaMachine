package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class ThirdTabController {

    @FXML
    private TextField currentConfiguration;

    @FXML
    private Button processBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private TextField encodeMsgTF;

    @FXML
    private TextField outputTF;

    @FXML
    private TextField searchInDictionaryTF;

    @FXML
    private Button clearBtn;

    @FXML
    private TextArea dictionaryTA;

    @FXML
    private Slider agentAmountSlider;

    @FXML
    private ChoiceBox<?> difficultyChoiceBox;

    @FXML
    private TextField taskSizeTF;

    @FXML
    private Button startBtn;

    @FXML
    private ToggleButton stopBtn;

    @FXML
    private ToggleButton pauseBtn;

    @FXML
    private ToggleButton resumeBtn;

    @FXML
    private TextArea candidatesTA;

    private MainPageController mainPageController;

    @FXML
    void encodeMsgListener(ActionEvent event) {

    }

    @FXML
    void pauseBtnListener(ActionEvent event) {

    }

    @FXML
    void processBtnListener(ActionEvent event) {

    }

    @FXML
    void resetBtnListener(ActionEvent event) {

    }

    @FXML
    void resumeBtnListener(ActionEvent event) {

    }

    @FXML
    void searchInDictionaryListener(ActionEvent event) {

    }

    @FXML
    void startBtnListener(ActionEvent event) {

    }

    @FXML
    void stopBtnListener(ActionEvent event) {

    }

    @FXML
    void taskSizeListener(ActionEvent event) {

    }

    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

}

