package application;

import dtos.DecodeStringInfo;
import exceptions.invalidInputException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

    private BooleanProperty decodedCorrectly = new SimpleBooleanProperty();

    private IntegerProperty integerProperty;


/*    @FXML private Label currentConfigurationLabel;

    @FXML private Label enterInputLabel;

    @FXML private Label outputLabel;*/
    @FXML private TextField currentConfiguration;
/*    @FXML private Label encryptDecryptLabel;
    @FXML private Label enterDecodedLabel;

    @FXML private Label decodedLabel;*/
    @FXML private TextField decodeResultTF;
    @FXML private Button processBtn;
    @FXML private Button clearBtn;
/*    @FXML private AnchorPane rightAP;
    @FXML private VBox rightVB;
    @FXML private Label statisticsLabel;
    @FXML private TextArea statisticsTA;*/
    @FXML private Button decodeLinebtn;
    @FXML private Button doneBtn;
    @FXML private Button decodeCharBtn;
    @FXML
    private TextField lineInputTF;
    @FXML
    private TextField charInputTF;

    @FXML private TextArea statisticsTA;

    public SecondTabController() {
        integerProperty = new SimpleIntegerProperty(0);
    }

    @FXML
    void processBtnListener(ActionEvent event) {
        decodedCorrectly.set(false);
        String tmp = lineInputTF.getText();
        if(!tmp.equals("")){
            try {
                DecodeStringInfo newInfo = mainPageController.decodeString(lineInputTF.getText());
                decodeResultTF.setText(newInfo.getDecodedString());
                //mainPageController.getEngine().getMachineStatistics().addDecodedStringInfo(newInfo);
                mainPageController.increaseDecodedStringAmount();
                decodedCorrectly.set(true);
                integerProperty.set(integerProperty.get()+1); // update times of decode.

                statisticsTA.appendText("   " + mainPageController.getCurrConfigurationDecodedAmount() +
                        ". <" + newInfo.getToEncodeString() + "> ----> <" + newInfo.getDecodedString() + "> (" + newInfo.getTimeInMilli() + " nano seconds)\n");
            } catch (invalidInputException e) {
                mainPageController.popUpError(e.getMessage());
            }
        }

    }

    @FXML
    void clearBtnListener(ActionEvent event) {
        clearTextFields();
    }

    @FXML
    void doneBtnListener(ActionEvent event) {
        decodedCorrectly.set(false);
        if(!charInputTF.getText().equals("")) {
            integerProperty.set(integerProperty.get()+1); // update times of decode.
            decodedCorrectly.set(true);
            mainPageController.increaseDecodedStringAmount();
            statisticsTA.appendText("   " + mainPageController.getCurrConfigurationDecodedAmount() +
                    ". <" + charInputTF.getText().toUpperCase() + "> ----> <" + decodeResultTF.getText() + "> ( NO nano seconds)\n");
        }

        clearTextFields();
    }

    @FXML
    void charInputListener(KeyEvent event) {
        char nextChar = 0;
        if(!event.getText().equals("")) {
            try {
                nextChar = event.getText().toUpperCase().charAt(0);
                decodeResultTF.setText(decodeResultTF.getText() + mainPageController.decodeChar(nextChar));
            }catch (RuntimeException ex) {//edge scenario - input from user is invalid so pop up msg
                mainPageController.popUpError("The letter " + nextChar + " doesn't exist in the language. Please try again.\n");
            }
        }
        //charInputTF.setText(charInputTF.getText() + event.getCharacter());
    }

    @FXML
    void decodeCharListener(ActionEvent event) {
        clearTextFields();

        lineInputTF.setDisable(true);
        processBtn.setDisable(true);
        clearBtn.setDisable(true);

        doneBtn.setDisable(false);

        charInputTF.setDisable(!lineInputTF.isDisable());

        shouldDecodeLine = false;
    }

    @FXML
    void decodeLineListener(ActionEvent event) {
        clearTextFields();

        lineInputTF.setDisable(false);
        processBtn.setDisable(false);
        clearBtn.setDisable(false);

        doneBtn.setDisable(true);

        charInputTF.setDisable(!lineInputTF.isDisable());

        shouldDecodeLine = true;
    }

    public IntegerProperty getAmountOfDecoding() {
        return this.integerProperty;
    }



    /*@FXML
    void lineInputListener(ActionEvent event) {

        String decodedString *//*= mainPageController.decodeString(lineInputTF.getText());*//* = "";
        decodeResultTF.setText(decodedString);
    }*/



    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public void setCurrentConfigurationLabel(String currConfiguration) {
        currentConfiguration.setText(currConfiguration);
    }


    public void setDecodingButtonsDisable(boolean setToDisable) {
        decodeLinebtn.setDisable(setToDisable);
        decodeCharBtn.setDisable(setToDisable);
        //clearBtn.setDisable(setToDisable);

    }


    public void clearTextFields(){
        charInputTF.clear();
        lineInputTF.clear();
        decodeResultTF.clear();
    }

    public TextArea getStatisticsTA(){
        return statisticsTA;
    }

    public void reLoadInitialize() {
        currentConfiguration.clear();
        statisticsTA.clear();

    }

    public void disableAllButtonsAndTextFields() {
        decodeCharBtn.setDisable(true);
        charInputTF.setDisable(true);
        doneBtn.setDisable(true);
        decodeLinebtn.setDisable(true);
        lineInputTF.setDisable(true);
        processBtn.setDisable(true);
        clearBtn.setDisable(true);
    }

}
