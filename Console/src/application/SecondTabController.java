package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SecondTabController {

    private MainPageController mainPageController;
    private boolean shouldDecodeLine; //false = should decode char

    @FXML private Label currentConfigurationLabel;

    @FXML private Label enterInputLabel;

    @FXML private Label outputLabel;
    @FXML private TextField currentConfiguration;
    @FXML private Label encryptDecryptLabel;
    @FXML private Label enterDecodedLabel;
    @FXML private TextField enterInputTF;
    @FXML private Label decodedLabel;
    @FXML private TextField decodeResultTF;
    @FXML private Button processBtn;
    @FXML private Button clearBtn;
    @FXML private AnchorPane rightAP;
    @FXML private VBox rightVB;
    @FXML private Label statisticsLabel;
    @FXML private TextArea statisticsTA;
    @FXML private Button decodeLinebtn;
    @FXML private Button decodeCharBtn;

    @FXML
    void actionOnProcessBtn(ActionEvent event) {

    }

    @FXML
    void clearBtnListener(ActionEvent event) { //not working right now, since the button disabled.
        enterInputTF.setText("");
        decodeResultTF.setText("");
    }

    @FXML
    void onKeyReleased(KeyEvent event) { //recognizes key pressed in textField, and also can recognize if it backspace or delete.
        if(event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE){
            System.out.println("Is working");
        }
        else{
            decodeResultTF.setText(event.getText());
        }
    }
    @FXML
    void onKeyPressed(KeyEvent event) {
      //  if(event.getCharacter() == )
      int x = 5;
    }

    @FXML
    void decodeCharListener(ActionEvent event) {
        enterInputTF.setDisable(false);
        shouldDecodeLine = false;
    }

    @FXML
    void decodeLineListener(ActionEvent event) {
        enterInputTF.setDisable(false);
        shouldDecodeLine = true;
    }

    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public void setCurrentConfigurationLabel(String currConfiguration) {
        currentConfiguration.setText(currConfiguration);
    }


    public void enableDecodingAndClearButtons() {
        decodeLinebtn.setDisable(false);
        decodeCharBtn.setDisable(false);
        clearBtn.setDisable(false);
    }
}
