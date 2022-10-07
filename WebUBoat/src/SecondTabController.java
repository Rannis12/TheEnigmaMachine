import dtos.DecodeStringInfo;
import exceptions.invalidInputException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public class SecondTabController {

    private MainPageController mainPageController;
    private boolean shouldDecodeLine; //false = should decode char

    @FXML private TextField currentConfiguration;
    @FXML private Button decodeLineBtn;
    @FXML private TextField lineInputTF;
    @FXML private Button readyBtn;
    @FXML private TextField decodeResultTF;
    @FXML private Button clearBtn;
    @FXML private Button resetBtn;
    @FXML private TextArea teamDetailsTA;
    @FXML
    private Button logoutBtn;
/*    @FXML
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

//                appendToStatistics("   " + mainPageController.getCurrConfigurationDecodedAmount() +
//                        ". <" + newInfo.getToEncodeString() + "> ----> <" + newInfo.getDecodedString() + "> (" + newInfo.getTimeInMilli() + " nano seconds)\n");
            } catch (invalidInputException e) {
                mainPageController.popUpError(e.getMessage());
            }
        }

    }*/

    @FXML
    void clearBtnListener(ActionEvent event) {
        clearEncryptTextFields();
    }

    @FXML
    void resetBtnListener(ActionEvent event) throws InterruptedException {
        mainPageController.resetEngineToUserInitChoice();
        mainPageController.updateConfigurationLabel();/*instead of this we can do ++ and then -- to
        the integerProperty and the listener will update automatically, but it's weird*/

        clearEncryptTextFields();
//        charInputTF.setDisable(true);
//        doneBtn.setDisable(true);
        lineInputTF.setDisable(true);
//        processBtn.setDisable(true);
        decodeResultTF.setText("Reset has been made successfully!!");

    }

    @FXML
    void decodeLineListener(ActionEvent event) {
        clearEncryptTextFields();

        lineInputTF.setDisable(false);
//        processBtn.setDisable(false);
        clearBtn.setDisable(false);

//        doneBtn.setDisable(true);

//        charInputTF.setDisable(!lineInputTF.isDisable());

        shouldDecodeLine = true;
    }

    @FXML
    void logoutBtnListener(ActionEvent event) {

    }
    @FXML
    void readyBtnListener(ActionEvent event) {

    }

    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public void setCurrentConfigurationTF(String currConfiguration) {
        currentConfiguration.setText(currConfiguration);
    }

    public void setDecodingButtonsDisable(boolean setToDisable) {
        decodeLineBtn.setDisable(setToDisable);
//        decodeCharBtn.setDisable(setToDisable);
        resetBtn.setDisable(setToDisable);

    }

    public void clearEncryptTextFields(){
//        charInputTF.clear();
        lineInputTF.clear();
        decodeResultTF.clear();
    }

    public void clearCurrentConfigurationTA() {
        currentConfiguration.clear();
    }

    public void disableAllButtonsAndTextFields() {
//        decodeCharBtn.setDisable(true);
//        charInputTF.setDisable(true);
//        doneBtn.setDisable(true);
        decodeLineBtn.setDisable(true);
        lineInputTF.setDisable(true);
//        processBtn.setDisable(true);
        clearBtn.setDisable(true);
    }






    /*public void appendToStatistics(String statisticNewString) {
        statisticsTA.appendText(statisticNewString);
    }*/

/*    @FXML
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

    }*/

/*    @FXML
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
    }*/

/*    @FXML
    void decodeCharListener(ActionEvent event) {
        clearEncryptTextFields();

        lineInputTF.setDisable(true);
//        processBtn.setDisable(true);
        clearBtn.setDisable(true);

//        doneBtn.setDisable(false);

//        charInputTF.setDisable(!lineInputTF.isDisable());

        shouldDecodeLine = false;
    }*/
    /*    public TextArea getStatisticsTA(){
        return statisticsTA;
    }*/
}
