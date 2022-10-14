package controllers;

//import client.http.HttpClientUtil;
import com.google.gson.Gson;
import dtos.DecodedStringAndConfigurationDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.*;

import java.io.IOException;

import static controllers.UBoatController.BASE_URL;


public class SecondTabController {

    private UBoatController UBoatController;
    private boolean shouldDecodeLine; //false = should decode char

    @FXML private TextField currentConfiguration;
    @FXML private Button decodeLineBtn;
    @FXML private TextField lineInputTF;
    @FXML private Button readyBtn;
    @FXML private TextField decodeResultTF;
    @FXML private Button clearBtn;
    @FXML private Button resetBtn;
    @FXML private Button processBtn;
    @FXML private TextArea teamDetailsTA;
    @FXML private Button logoutBtn;
    @FXML
    void processBtnListener(ActionEvent event) {
        UBoatController.setDecodedCorrectly(false);
        String tmp = lineInputTF.getText().toUpperCase();

        if(!tmp.equals("")) {
            try {
                String resourceName = "/decode-string";
                HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + resourceName).newBuilder();
                urlBuilder.addQueryParameter("toDecode", tmp);
                String finalUrl = urlBuilder.build().toString();

                Request request = new Request.Builder()
                        .url(finalUrl)
                        .get()
                        .build();

                Call call = new OkHttpClient().newCall(request);


                Response response = call.execute();
                String json = response.body().string();

                Gson gson = new Gson();
                DecodedStringAndConfigurationDTO dto = gson.fromJson(json, DecodedStringAndConfigurationDTO.class);

                decodeResultTF.setText(dto.getDecodedString());
                UBoatController.setTabsConfiguration(dto.getCurrConfiguration());

                UBoatController.setDecodedCorrectly(true);

            }catch(IOException e){
                throw new RuntimeException(e);
            }


//                DecodeStringInfo newInfo = mainPageController.decodeString(lineInputTF.getText());
//                decodeResultTF.setText(newInfo.getDecodedString());
//                mainPageController.increaseDecodedStringAmount();//this is for the current configuration amount of decoded strings(output in statistics)



//                mainPageController.setAmountOfDecodedStrings(mainPageController.getAmountOfDecodedStrings() + 1);

//                appendToStatistics("   " + mainPageController.getCurrConfigurationDecodedAmount() +
//                        ". <" + newInfo.getToEncodeString() + "> ----> <" + newInfo.getDecodedString() + "> (" + newInfo.getTimeInMilli() + " nano seconds)\n");
//            } catch (invalidInputException e) {
//                mainPageController.popUpError(e.getMessage());
//            }
//        }

        }
    }

    @FXML
    void clearBtnListener(ActionEvent event) {
        clearEncryptTextFields();
    }

    @FXML
    void resetBtnListener(ActionEvent event) throws InterruptedException {

        Request request = new Request.Builder()
                .url(BASE_URL + "/reset-engine")
                .get()
                .build();

        Call call = new OkHttpClient().newCall(request);

        try {
            Response response = call.execute();
            String conf = response.body().string();
            conf = conf.trim();

            UBoatController.setTabsConfiguration(conf);
        } catch (IOException e) {
            throw new RuntimeException("error in reset the engine");
        }

//        UBoatController.updateConfigurationLabel();/*instead of this we can do ++ and then -- to
//        the integerProperty and the listener will update automatically, but it's weird*/

        clearEncryptTextFields();
        lineInputTF.setDisable(true);

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

    public void setMainPageController(UBoatController UBoatController) {
        this.UBoatController = UBoatController;
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

    public TextField getConfigurationTF() {
        return currentConfiguration;
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
