package application;

import dtos.DecodeStringInfo;
import exceptions.invalidInputException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import okhttp3.*;

import java.io.IOException;


public class SecondTabController {
    private MainPageController mainPageController;
    private boolean shouldDecodeLine; //false = should decode char
    @FXML private TextField currentConfiguration;
    @FXML private TextField decodeResultTF;
    @FXML private Button processBtn;
    @FXML private Button clearBtn;
    @FXML private Button resetBtn;
    @FXML private Button decodeLineBtn;
    @FXML private Button doneBtn;
    @FXML private Button decodeCharBtn;
    @FXML private TextField lineInputTF;
    @FXML private TextField charInputTF;
    @FXML private TextArea statisticsTA;

        @FXML
        void processBtnListener(ActionEvent event) {
            mainPageController.setDecodedCorrectly(false);

            String tmp = lineInputTF.getText();
            if(!tmp.equals("")){
                try {
                    DecodeStringInfo newInfo = mainPageController.decodeString(lineInputTF.getText());
                    decodeResultTF.setText(newInfo.getDecodedString());
                    mainPageController.increaseDecodedStringAmount();//this is for the current configuration amount of decoded strings(output in statistics)
                    mainPageController.setDecodedCorrectly(true);


                    mainPageController.setAmountOfDecodedStrings(mainPageController.getAmountOfDecodedStrings() + 1);

                } catch (invalidInputException e) {
                    mainPageController.popUpError(e.getMessage());
                }
            }

        }


    @FXML
    void clearBtnListener(ActionEvent event) {
        clearEncryptTextFields();
    }
    public void clearCurrentConfigurationTA() {
        currentConfiguration.clear();
    }

    @FXML
    void resetBtnListener(ActionEvent event) throws InterruptedException {
        mainPageController.resetEngineToUserInitChoice();
        mainPageController.updateConfigurationLabel();/*instead of this we can do ++ and then -- to
        the integerProperty and the listener will update automatically, but it's weird*/

        clearEncryptTextFields();
        charInputTF.setDisable(true);
        doneBtn.setDisable(true);
        lineInputTF.setDisable(true);
        processBtn.setDisable(true);
        decodeResultTF.setText("Reset has been made successfully!!");
    }

    @FXML
    void doneBtnListener(ActionEvent event) {
        mainPageController.setDecodedCorrectly(false);
        if(!charInputTF.getText().equals("")) {
            mainPageController.setAmountOfDecodedStrings(mainPageController.getAmountOfDecodedStrings() + 1);

            mainPageController.setDecodedCorrectly(true);

            mainPageController.increaseDecodedStringAmount();
            appendToStatistics("   " + mainPageController.getCurrConfigurationDecodedAmount() +
                    ". <" + charInputTF.getText().toUpperCase() + "> ----> <" + decodeResultTF.getText() + "> ( NO nano seconds)\n");
        }

        clearEncryptTextFields();
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
    }

    @FXML
    void decodeCharListener(ActionEvent event) {
        clearEncryptTextFields();

        lineInputTF.setDisable(true);
        processBtn.setDisable(true);
        clearBtn.setDisable(true);

        doneBtn.setDisable(false);

        charInputTF.setDisable(!lineInputTF.isDisable());

        shouldDecodeLine = false;
    }

    @FXML
    void decodeLineListener(ActionEvent event) {
        clearEncryptTextFields();

        lineInputTF.setDisable(false);
        processBtn.setDisable(false);
        clearBtn.setDisable(false);

        doneBtn.setDisable(true);

        charInputTF.setDisable(!lineInputTF.isDisable());

        shouldDecodeLine = true;
    }

    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public void setCurrentConfigurationTF(String currConfiguration) {
        currentConfiguration.setText(currConfiguration);
    }

    public void setDecodingButtonsDisable(boolean setToDisable) {
        decodeLineBtn.setDisable(setToDisable);
        decodeCharBtn.setDisable(setToDisable);
        resetBtn.setDisable(setToDisable);

    }

    public void clearEncryptTextFields(){
        charInputTF.clear();
        lineInputTF.clear();
        decodeResultTF.clear();
    }

    public TextArea getStatisticsTA(){
        return statisticsTA;
    }

    public void disableAllButtonsAndTextFields() {
        decodeCharBtn.setDisable(true);
        charInputTF.setDisable(true);
        doneBtn.setDisable(true);
        decodeLineBtn.setDisable(true);
        lineInputTF.setDisable(true);
        processBtn.setDisable(true);
        clearBtn.setDisable(true);
    }

    public void appendToStatistics(String statisticNewString) {
        statisticsTA.appendText(statisticNewString);
    }
}
